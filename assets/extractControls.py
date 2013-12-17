import urllib.request
import sys
import json
import glob
import os
import sqlite3

#http://countries.epg.samsung.peel.com/countries/all  #contrys
#http://countries.epg.samsung.peel.com/countries/info/${COUNTRY}  #contry infos


#
#http://samsungir.peel.com/epg/schedules/providerbrands/%{s} #get brands by provider #google, sky, net?
#http://samsungir.peel.com/targets/brands/${i}?country=${COUNTRY}  #i=type, get brands by (device) type
#http://samsungir.peel.com/targets/brands/codesets/%{d}?devicetypeid=%{d}&country=${COUNTRY} #getCodesets
#http://samsungir.peel.com/targets/uesid/uesidsforfunction/${Command}?devicetypeid=$i&brandid=${brandID}&country=${COUNTRY} # getAllPossibleIrByFuncNameSorted, ex:Comman=Power
#http://samsungir.peel.com/targets/uesid/uesidsforcodeset/${devID}  #dgetAllIrCodesByCodesetid


def downCodeSet(controlOriId,country="US"):
	if not os.path.exists('controls'):
		os.makedirs('controls')
	filePath="controls/{}.json".format(controlOriId)
	if os.path.isfile(filePath): #cache
		data = open(filePath,'r')
		return json.load(data)
	else:
		url = 'http://samsungir.peel.com/targets/uesid/uesidsforcodeset/{}'.format(controlOriId)
		user_agent = 'peel'
		headers = { 'User-Agent' : user_agent }
		req = urllib.request.Request(url, None, headers)
		response = urllib.request.urlopen(req)
		data=response.read().decode("utf8")
		output = open(filePath,'w')
		output.write(data)
		output.close()
		return json.loads(data)

def downControls(categoryOriId,brandOriId,country="US"):
	if not os.path.exists(str(categoryOriId)):
		os.makedirs(str(categoryOriId))
	filePath="{}/{}.json".format(categoryOriId,brandOriId)
	if os.path.isfile(filePath): #cache
		data = open(filePath,'r')
		return json.load(data)
	else:			
		url = 'http://samsungir.peel.com/targets/brands/codesets/{}?devicetypeid={}&country={}'.format(brandOriId,categoryOriId,country)
		user_agent = 'peel'
		headers = { 'User-Agent' : user_agent }
		req = urllib.request.Request(url, None, headers)
		response = urllib.request.urlopen(req)
		data=response.read().decode("utf8")
		output = open(filePath,'w')
		output.write(data)
		output.close()
		return json.loads(data)

def downBrands(categoryOriId,country="US"):
	filePath="{}.json".format(categoryOriId)
	if os.path.isfile(filePath): #cache
		data = open(filePath,'r')
		return json.load(data)
	else:			
		url = 'http://samsungir.peel.com/targets/brands/{}?country={}'.format(categoryOriId,country)
		user_agent = 'peel'
		headers = { 'User-Agent' : user_agent }
		req = urllib.request.Request(url, None, headers)
		response = urllib.request.urlopen(req)
		data=response.read().decode("utf8")
		output = open(filePath,'w')
		output.write(data)
		output.close()
		return json.loads(data)


def addControlCategory(conn,categoryId,controlId):
	cursor = conn.cursor()
	insertBrand=[categoryId,controlId]
	cursor.execute("insert into categoryControl (_idCategory,_idControl) values (?,?)",insertBrand)
	conn.commit()
	return cursor.lastrowid

def addCodSet(conn,Data):
	cursor = conn.cursor()
	cursor.execute("insert into command (idControl,UES,inputflag,funId,funName,funDisplayName,desType,frequency,repeatcount,mainframe,repeatframe,toggleframe1,toggleframe2,toggleframe3,toggleframe4,endframe) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Data)
	conn.commit()

def addControls(conn,categoryId,brandId,controlOriId):
	cursor = conn.cursor()
	controlId=-1
	selectWhere=[controlOriId,brandId]
	cursor.execute('select _id from control where oriId = ? and idBrand = ? limit 1',selectWhere)
	row=cursor.fetchone()
	exist=False
	if row == None: # id Control don't exist create
		insertControl=[controlOriId,brandId,str(controlOriId)]
		cursor.execute("insert into control (oriId,idBrand,name) values (?,?,?)",insertControl)
		conn.commit()
		controlId=cursor.lastrowid
		addControlCategory(conn,categoryId,controlId)		
	else:
		exist=True
		controlId=row[0]
		insertControl=[controlId,categoryId]
		cursor.execute('select _idCategory,_idControl from categoryControl where _idControl = ? and _idCategory = ?',insertControl)
		row=cursor.fetchone()
		if row == None: # if brand not associate with category associate
			addControlCategory(conn,categoryId,controlId)

	return [controlId,exist]

def addBrandCategory(conn,categoryId,brandId):
	cursor = conn.cursor()
	insertBrand=[categoryId,brandId]
	cursor.execute("insert into categoryBrand (_idCategory,_idBrand) values (?,?)",insertBrand)
	conn.commit()
	return cursor.lastrowid
		
def addBrands(conn,categoryId,categoryOriId,brandOriId,brandName):
	cursor = conn.cursor()
	brandId=-1
	selectBrand=[brandOriId,brandName]
	cursor.execute('select _id from brand where oriId = ? or name = ? limit 1',selectBrand)
	row=cursor.fetchone()
	if row == None: # id brand don't exist create
		insertBrand=[brandOriId,brandName]
		cursor.execute("insert into brand (oriId,name) values (?,?)",insertBrand)
		conn.commit()
		brandId=cursor.lastrowid
		addBrandCategory(conn,categoryId,brandId)		
	else:
		brandId=row[0]
		insertBrand=[brandId,categoryId]
		cursor.execute('select _idCategory,_idBrand from categoryBrand where _idBrand = ? and _idCategory = ?',insertBrand)
		row=cursor.fetchone()
		if row == None: # if brand not associate with category associate
			addBrandCategory(conn,categoryId,brandId)

	return brandId

def addCategory(conn,categoryOriId):
	cursor = conn.cursor()
	insertCategory=[categoryOriId,str(categoryOriId)]
	cursor.execute('insert into category (oriId,name) values (?,?)', insertCategory)
	conn.commit()
	return cursor.lastrowid
	
	
def getCodSets(conn,controlId,controlOriId):
	codSets=downCodeSet(controlOriId)
	if type(codSets) is not dict:
		return
	if type(codSets['UESCodeset']) is list:
		for codSet in codSets['UESCodeset']:
			insertCodSet=[controlId,int(codSet['UES']),codSet['inputflag'],int(codSet['funId']),codSet['funName'],codSet['funDisplayName'],codSet['uesdata']['type'],int(codSet['uesdata']['frequency']),int(codSet['uesdata']['repeatcount']),codSet['uesdata']['mainframe'],codSet['uesdata']['repeatframe'],codSet['uesdata']['toggleframe1'],codSet['uesdata']['toggleframe2'],codSet['uesdata']['toggleframe3'],codSet['uesdata']['toggleframe4'],codSet['uesdata']['endframe']]		
			addCodSet(conn,insertCodSet)			
	elif type(codSets['UESCodeset']) is dict:
			codSet=codSets['UESCodeset'];
			insertCodSet=[controlId,int(codSet['UES']),codSet['inputflag'],int(codSet['funId']),codSet['funName'],codSet['funDisplayName'],codSet['uesdata']['type'],int(codSet['uesdata']['frequency']),int(codSet['uesdata']['repeatcount']),codSet['uesdata']['mainframe'],codSet['uesdata']['repeatframe'],codSet['uesdata']['toggleframe1'],codSet['uesdata']['toggleframe2'],codSet['uesdata']['toggleframe3'],codSet['uesdata']['toggleframe4'],codSet['uesdata']['endframe']]
			addCodSet(conn,insertCodSet)
	else:
		print(codSets)
		print("\t\t\tCodSet ERRO$")
		

def getControls(conn,categoryId,categoryOriId,brandId,brandOriId):
	controls=downControls(categoryId,brandOriId)
	if type(controls) is not dict:
		print("{}\t\tControl Erro1".format(categoryId))
		return
	if type(controls['Codeset']) is list:
		for control in controls['Codeset']:
			controlOriId=int(control['codesetid'])
			print("{}\t\tControl {}".format(categoryId,controlOriId))
			controlId,exist=addControls(conn,categoryId,brandId,controlOriId)
			if exist == False: #if exist not add other
				getCodSets(conn,controlId,controlOriId)
	elif type(controls['Codeset']) is dict:
		controlOriId=int(controls['Codeset']['codesetid'])
		print("{}\t\tControl {}".format(categoryId,controlOriId))
		controlId,exist=addControls(conn,categoryId,brandId,controlOriId)
		if exist == False: #if exist not add other
			getCodSets(conn,controlId,controlOriId)
	else:
		print(controls)
		print("{}\t\tControl ERRO$".format(categoryId))
		
		

def getBrands(conn,categoryId,categoryOriId,country="US"):
	brands = downBrands(categoryOriId)
	if type(brands) is not dict:
		print("{}\t Brand Erro1".format(categoryId))
		return
	if type(brands['brands']) is not dict:
		print("{}\t Brand Erro2".format(categoryId))
		return		
	brands=brands['brands']['brands']
	if type(brands) is list:
		for brand in brands:
			print("{}\t Brand {}".format(categoryId,brand['brandName']))
			brandId=addBrands(conn,categoryId,categoryOriId,int(brand['id']),brand['brandName'])
			getControls(conn,categoryId,categoryOriId,brandId,int(brand['id']))
	elif type(brands) is dict:
		print("{}\tBrand {}".format(categoryId,brands['brandName']))
		brandId=addBrands(conn,categoryId,categoryOriId,int(brands['id']),brands['brandName'])
		getControls(conn,categoryId,categoryOriId,brandId,int(brands['id']))
	else:
		print(brands)
		print("{}\tBrand ERRO$".format(categoryId))

def getCategories(conn):
	for categoryOriId in range(1,23):
		print("Category {}".format(categoryOriId))
		categoryId=addCategory(conn,categoryOriId)
		brands=getBrands(conn,categoryId,categoryOriId)


if __name__ == "__main__":	
	conn = sqlite3.connect('remotes.sqlite')
	
	cursor = conn.cursor()
	
	cursor.execute("create table category(_id integer not null,oriId integer not null,name varchar(64),PRIMARY KEY (_id))")
	conn.commit()	

	cursor.execute("create table categoryBrand(_idCategory integer not null,_idBrand integer not null,PRIMARY KEY (_idCategory,_idBrand),FOREIGN KEY(_idCategory) REFERENCES category(_id),FOREIGN KEY(_idBrand) REFERENCES brand(_id))")
	conn.commit()

	cursor.execute("create table brand(_id integer not null,oriId integer not null,name varchar(64),PRIMARY KEY (_id))")
	conn.commit()

	cursor.execute("create table categoryControl(_idCategory integer not null,_idControl integer not null,PRIMARY KEY (_idCategory,_idControl),FOREIGN KEY(_idCategory) REFERENCES category(_id),FOREIGN KEY(_idControl) REFERENCES control(_id))")
	conn.commit()

	cursor.execute("create table control(_id integer not null,oriId integer not null,idBrand integer not null,name varchar(64),PRIMARY KEY (_id),FOREIGN KEY(idBrand) REFERENCES brand(_id))")
	conn.commit()

	cursor.execute("create table command(_id integer not null,idControl integer not null,UES integer not null,inputflag varchar(1),funId integer,funName varchar,funDisplayName varchar,desType varchar,frequency integer not null,repeatcount integer not null,mainframe varchar(1024),repeatframe varchar(512),toggleframe1 varchar(1024),toggleframe2 varchar(1024),toggleframe3 varchar(1024),toggleframe4 varchar(1024),endframe varchar(1024),PRIMARY KEY (_id),FOREIGN KEY(idControl) REFERENCES control(_id))")
	conn.commit()

	cursor.execute("CREATE TABLE android_metadata (locale TEXT DEFAULT 'en_US')")
	conn.commit()
	
	cursor.execute("INSERT INTO android_metadata VALUES ('en_US')")
	conn.commit()
	
	
	getCategories(conn)		
		
	conn.close();
		
	#conn = sqlite3.connect("remotes.sqlite3") 
	#cursor = conn.cursor()
	
	
	
