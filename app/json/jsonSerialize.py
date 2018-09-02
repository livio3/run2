#Written by Andysnake96
#serialize races info in json by json lib & python dict into jsonGare.txt
#dict <--->json			-> = json.dump(dict,filePointer)
#				<- = json.loads(jsonStr)-> list of dict....
#jsons will be avaible on https://github.com/andysnake96/RUN2SERVER ...
import json

races=list()					#glbl list of race to serialize in json...
raceKeys=('dateRace', 'description', 'distance','id_race', 'locality', 'n_max_runner', 'name', 'note', 'prenExpire', 'urlImage', 'urlRace')

jsonFile=open("jsonGare.txt","w")		#dest output file with serialized json

def addRace(dateRace, description, distance, id_race, locality, n_max_runner, name, note, prenExpire, urlRace,urlImage ):
	#create dict rappresenting race entity from values...
	print(raceKeys)
	race=dict()
	race['dateRace']=dateRace
	race['description']=description
	race['distance']=distance
	race['id_race']=id_race
	race['locality']=locality
	race['n_max_runner']=n_max_runner
	race['name']=name
	race['note']=note
	race['prenExpire']=prenExpire
	race['urlImage']=urlImage
	race['urlRace']=urlRace
	
	races.append(race)			#added to races to serialize


def serialize():
	#serialize writted race into json file pointed from jsonFile 
	json.dump(races,jsonFile)
	jsonFile.close()

if __name__=="__main__":
	#adding races...
	addRace("2018-09-23-09:00","2nda ed. mezza maratona via pacis",21.097,0,"Roma",300,"Rome Half Marathon Via Pacis 2nda edizione","occhio alle buche","2018-09-17-00:00","http://www.romahalfmarathon.org","http://www.fidal.it/funzioni/phpThumb/phpThumb.php?src=http://www.fidal.it/upload/Viapacis/20170914_1_giorno_0004.JPG&w=940&h=460&zc=1&hash=f592be057557474f87d868d5dc9ce5d2")
	
	addRace("2018-09-30-09:30","quarta ed. cardio race ",16.092,2,"Roma",300,"cardio race ed.4 2018","corri per la salute","2018-09-19-00:00","http://www.cardiorace.it/","https://www.cardiorace.it/wp-content/uploads/2018/04/logo-cuore-2-600x226.jpg")

	addRace("2018-09-23-17:30","Corsa podistica Ortona ",21.000,3,"Ortona",150,"Ortona Chanllenge","corri per la salute","2018-09-19-00:00","http://armyrun.ca/race-weekend/ortona-challenge/","https://pbs.twimg.com/media/DfCAYuQXUAETvjM.jpg")

	addRace("2018-08-11-18:30","Gara podistica tra le Cascate Rio Verde",10.2,4,"Borrello",110,"Gara Podistica Cascate Rio Verde","piano sulle salite!","2018-08-11-10:00","","http://www.cascatedelverde.it/cascatedelverde/images/stories/Immagini_Sito_Cascate/RioVerde.jpg")

	addRace("2018-09-13-10:30","Missione corsa",10.0,5,"GROTTAFERRATA (RM)",100,"Mission Run 2018","corri per la salute","2018-09-5-00:00","http://www.missionrun.it/","https://s3-eu-west-1.amazonaws.com/ecs-gruppi/00000000/00010000/00012100/00012162/images/logo.jpg")

	addRace("2018-09-14-10:00","Keep On Running",13.0,6,"PASSO CORESE (RI)",300,"Corricures","corri per la salute","2018-09-7-00:00","http://www.corricures.it/","http://admin.101sport.net/upload/www.corricures.it/5caeadb5-039f.jpg")

	
	addRace("2018-11-14-9:30","Gara podistica a Terracina",13.0,7,"TERRACINA (LT)",300,"Corri a terracina","corsa a terracina","2018-09-30-00:00","http://www.uisp.it/latina2/","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ3f9cm9-P-rzxgMAZfzaM6OVP1SugrAqEeGF_Zf7GdOkzWBg1A")


	serialize()
