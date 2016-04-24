package com.jobsearch.model;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.jobsearch.job.service.CreateJobDTO;
import com.jobsearch.job.service.Job;
import com.jobsearch.user.service.JobSearchUser;
import com.jobsearch.utilities.DateUtility;
import com.jobsearch.utilities.MathUtility;


public class DummyData {
	
	Random random = new Random();
	
	List<DummyCity> cities;
	List<String> firstNames;
	List<String> lastNames;
	
	public List<CreateJobDTO> getDummyJobs(List<JobSearchUser> dummyEmployers ){
		setJobCities();
		
		List<CreateJobDTO> dummyJobs = new ArrayList<CreateJobDTO>();

		int jobCount = 0;
		for(JobSearchUser employer : dummyEmployers){
			
			
//			for(int i = 0;i < dummyEmployers.size(); i++){
				CreateJobDTO createJobDTO = new CreateJobDTO();
				createJobDTO.setJobName("Job " + jobCount);
				createJobDTO.setUserId(employer.getUserId());
				
				DummyCity dummyCity = getRandomDummyCity(cities);
				createJobDTO.setCity(dummyCity.name);
				createJobDTO.setState(dummyCity.state);
				createJobDTO.setLat(getLatOrLng(dummyCity.population, dummyCity.lat));
				createJobDTO.setLng(getLatOrLng(dummyCity.population, dummyCity.lng));
				createJobDTO.setStartDate(getStatDate());
				createJobDTO.setEndDate(getEndDate(createJobDTO.getStartDate()));
				createJobDTO.setStartTime(getStartTime());
				createJobDTO.setEndTime(getEndTime(createJobDTO.getStartTime()));
				
				jobCount += 1;
				dummyJobs.add(createJobDTO);
				
				
				
//			}
		}
		
		
		return dummyJobs;
	}

		
	private Time getEndTime(Time startTime) {
	
		double randomNumber = random.nextDouble();
		int hoursToAdd;
		if (randomNumber < 0.1){
			hoursToAdd = 6;
		}else if(randomNumber < 0.25){
			hoursToAdd = 7;
		}else if(randomNumber < 0.6){
			hoursToAdd = 8;
		}else if(randomNumber < 0.85){
			hoursToAdd = 9;
		}else{
			hoursToAdd = 10;
		}
		
		Calendar c = Calendar.getInstance();
		c.setTime(startTime);
		c.add(Calendar.HOUR_OF_DAY, hoursToAdd);
		
		DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
		return java.sql.Time.valueOf(dateFormat.format(c.getTime()));
				
	}


	private Time getStartTime() {
		
		String startTime;
		double randomNumber = random.nextDouble();
		if (randomNumber < 0.1){
			startTime = "06:30:00";
		}else if(randomNumber < 0.25){
			startTime = "07:00:00";
		}else if(randomNumber < 0.5){
			startTime = "07:30:00";
		}else if(randomNumber < 0.75){
			startTime = "08:00:00";
		}else if(randomNumber < 0.9){
			startTime = "08:30:00";
		}else{
			startTime = "09:00:00";
		}
				
		return java.sql.Time.valueOf(startTime);
		
	}


	private java.sql.Date getEndDate(java.sql.Date startDate) {
		
		
		int randomDaysToAdd = (int) (1 + MathUtility.round(10 * random.nextDouble(), 0, 0));
		
		Calendar c = Calendar.getInstance();
		c.setTime(startDate); // Now use today date.
		c.add(Calendar.DATE, randomDaysToAdd); // Adding 5 days
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return DateUtility.getSqlDate(dateFormat.format(c.getTime()));

	}


	private java.sql.Date getStatDate() {
		
		
		
		int randomDaysToAdd = (int) MathUtility.round(10 * random.nextDouble(), 0, 0);
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); // Now use today date.
		c.add(Calendar.DATE, randomDaysToAdd); // Adding 5 days
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return DateUtility.getSqlDate(dateFormat.format(c.getTime()));
					
		//Convert strings to sql Time objects
//		jobDto.setStartTime(java.sql.Time.valueOf(jobDto.getStringStartTime()));
//		jobDto.setEndTime(java.sql.Time.valueOf(jobDto.getStringEndTime()));

	}


	private float getLatOrLng(int cityPopulation, double cityCoord) {
		
		
		//For example if a city has 250,000 people, the the allowed variance is 0.17.
		//If the city has a latitude of 43.000, then the returned latitude will be
		//between 42.915 and 43.085.
		
		double allowedVariance; 
		if(cityPopulation > 2000000){
			allowedVariance = .17;
		}else if(cityPopulation > 80000){
			allowedVariance = .14;
		}else if(cityPopulation > 40000){
			allowedVariance = .01;
		}else if(cityPopulation > 20000){
			allowedVariance = .08;
		}else {
			allowedVariance = .04;
		}
		
		//For example, if allowedVariance = 0.17 and the random number = 0.25,
		//then randomDisplacement = -0.085 + 0.17 * .25 = -0.085 + 0.0425 = -0.0425
		double randomNumber = random.nextDouble();
		
		double randomDisplacement = -1 * (allowedVariance / 2) + (allowedVariance) * randomNumber;
		System.out.println("Random number is " + randomNumber + "; Displacement is " + randomDisplacement); 
		
		
		return (float) (cityCoord + randomDisplacement);
		
	}


	private DummyCity getRandomDummyCity(List<DummyCity> cities) {
		
		int totalPopulation = cities.stream().mapToInt(o -> o.population).sum();
		int randomNumber = random.nextInt(totalPopulation);
		
		int countPopulation = 0;
		int countCities = -1;
		
		while (countPopulation <= randomNumber){
			countCities += 1;
			countPopulation += cities.get(countCities).population;			
		}
		
		return cities.get(countCities);
	}


	public List<JobSearchUser> getDummyUsers(){
		setUserCities();
		setLastNames();
		setFirstNames();
		
		GoogleClient maps = new GoogleClient();
		
		List<JobSearchUser> dummyUsers = new ArrayList<JobSearchUser>();
		
		for(DummyCity city : cities){
			for(int i = 0;i < city.userCount; i++){
				JobSearchUser user = new JobSearchUser();
				user.setFirstName(getFirstName());
				user.setLastName(getLastName());
				user.setEmailAddress(getEmail(user.getFirstName() + user.getLastName()));
				user.setPassword(user.getFirstName());
				user.setProfileId(getProfileId());
				user.setRating(getRating());
				user.setHomeCity(city.name);
				user.setHomeState(city.state);
				
				user.setHomeLat((float) city.lat);
				user.setHomeLng((float) city.lng);
				user.setMaxWorkRadius(getMaxWorkRadius());
				dummyUsers.add(user);
				
				
				
			}
		}
		
		return dummyUsers;
	
	
	}
	
	private double getRating() {
		
		double randomNumber = random.nextDouble();
		
		return randomNumber * 5.0;
	}

	private int getMaxWorkRadius(){
		double randomNumber = random.nextDouble();
		
		if(randomNumber < .1){
			return 15;
		}else if(randomNumber < .3){
			return 25;
		}else if(randomNumber < .6){
			return 35;
		}else if(randomNumber < .9){
			return 45;	
		}else {
			return 55;
		}
	}
	
	private int getProfileId(){
		double employeeWeight = .7;
		
		double randomNumber = random.nextDouble();
		
		if(randomNumber < employeeWeight){
			return 2;
		}else{
			return 1;
		}
	}
	
	private String getEmail(String salt){
		return salt + random.nextInt(500);
	}
	
	
	private String getLastName(){		
		return lastNames.get(random.nextInt(lastNames.size()));
	}
	

	private String getFirstName(){		
		return firstNames.get(random.nextInt(firstNames.size()));
	}
	
	
	private class DummyCity{
		String name;
		int population;
		int userCount;
		String state;
		double lat;
		double lng;
		
		public DummyCity(String name, int population, int userCount, String state,
							double lat, double lng){
			
			this.name = name;
			this.population = population;
			this.userCount = userCount;
			this.state = state;
			this.lat = lat;
			this.lng = lng;
		}

	}
	
	private void setFirstNames(){
		
		firstNames = new ArrayList<String>();
		firstNames.add("James");
		firstNames.add("John");
		firstNames.add("Robert");
		firstNames.add("Michael");
		firstNames.add("William");
		firstNames.add("David");
		firstNames.add("Richard");
		firstNames.add("Charles");
		firstNames.add("Joseph");
		firstNames.add("Thomas");
		firstNames.add("Christopher");
		firstNames.add("Daniel");
		firstNames.add("Paul");
		firstNames.add("Mark");
		firstNames.add("Donald");
		firstNames.add("George");
		firstNames.add("Kenneth");
		firstNames.add("Steven");
		firstNames.add("Edward");
		firstNames.add("Brian");
		firstNames.add("Ronald");
		firstNames.add("Anthony");
		firstNames.add("Kevin");
		firstNames.add("Jason");
		firstNames.add("Matthew");
		firstNames.add("Gary");
		firstNames.add("Timothy");
		firstNames.add("Jose");
		firstNames.add("Larry");
		firstNames.add("Jeffrey");
		firstNames.add("Frank");
		firstNames.add("Scott");
		firstNames.add("Eric");
		firstNames.add("Stephen");
		firstNames.add("Andrew");
		firstNames.add("Raymond");
		firstNames.add("Gregory");
		firstNames.add("Joshua");
		firstNames.add("Jerry");
		firstNames.add("Dennis");
		firstNames.add("Walter");
		firstNames.add("Patrick");
		firstNames.add("Peter");
		firstNames.add("Harold");
		firstNames.add("Douglas");
		firstNames.add("Henry");
		firstNames.add("Carl");
		firstNames.add("Arthur");
		firstNames.add("Ryan");
		firstNames.add("Roger");
		firstNames.add("Joe");
		firstNames.add("Juan");
		firstNames.add("Jack");
		firstNames.add("Albert");
		firstNames.add("Jonathan");
		firstNames.add("Justin");
		firstNames.add("Terry");
		firstNames.add("Gerald");
		firstNames.add("Keith");
		firstNames.add("Samuel");
		firstNames.add("Willie");
		firstNames.add("Ralph");
		firstNames.add("Lawrence");
		firstNames.add("Nicholas");
		firstNames.add("Roy");
		firstNames.add("Benjamin");
		firstNames.add("Bruce");
		firstNames.add("Brandon");
		firstNames.add("Adam");
		firstNames.add("Harry");
		firstNames.add("Fred");
		firstNames.add("Wayne");
		firstNames.add("Billy");
		firstNames.add("Steve");
		firstNames.add("Louis");
		firstNames.add("Jeremy");
		firstNames.add("Aaron");
		firstNames.add("Randy");
		firstNames.add("Howard");
		firstNames.add("Eugene");
		firstNames.add("Carlos");
		firstNames.add("Russell");
		firstNames.add("Bobby");
		firstNames.add("Victor");
		firstNames.add("Martin");
		firstNames.add("Ernest");
		firstNames.add("Phillip");
		firstNames.add("Todd");
		firstNames.add("Jesse");
		firstNames.add("Craig");
		firstNames.add("Alan");
		firstNames.add("Shawn");
		firstNames.add("Clarence");
		firstNames.add("Sean");
		firstNames.add("Philip");
		firstNames.add("Chris");
		firstNames.add("Johnny");
		firstNames.add("Earl");
		firstNames.add("Jimmy");
		firstNames.add("Antonio");
		firstNames.add("Danny");
		firstNames.add("Bryan");
		firstNames.add("Tony");
		firstNames.add("Luis");
		firstNames.add("Mike");
		firstNames.add("Stanley");
		firstNames.add("Leonard");
		firstNames.add("Nathan");
		firstNames.add("Dale");
		firstNames.add("Manuel");
		firstNames.add("Rodney");
		firstNames.add("Curtis");
		firstNames.add("Norman");
		firstNames.add("Allen");
		firstNames.add("Marvin");
		firstNames.add("Vincent");
		firstNames.add("Glenn");
		firstNames.add("Jeffery");
		firstNames.add("Travis");
		firstNames.add("Jeff");
		firstNames.add("Chad");
		firstNames.add("Jacob");
		firstNames.add("Lee");
		firstNames.add("Melvin");
		firstNames.add("Alfred");
		firstNames.add("Kyle");
		firstNames.add("Francis");
		firstNames.add("Bradley");
		firstNames.add("Jesus");
		firstNames.add("Herbert");
		firstNames.add("Frederick");
		firstNames.add("Ray");
		firstNames.add("Joel");
		firstNames.add("Edwin");
		firstNames.add("Don");
		firstNames.add("Eddie");
		firstNames.add("Ricky");
		firstNames.add("Troy");
		firstNames.add("Randall");
		firstNames.add("Barry");
		firstNames.add("Alexander");
		firstNames.add("Bernard");
		firstNames.add("Mario");
		firstNames.add("Leroy");
		firstNames.add("Francisco");
		firstNames.add("Marcus");
		firstNames.add("Micheal");
		firstNames.add("Theodore");
		firstNames.add("Clifford");
		firstNames.add("Miguel");
		firstNames.add("Oscar");
		firstNames.add("Jay");
		firstNames.add("Jim");
		firstNames.add("Tom");
		firstNames.add("Calvin");
		firstNames.add("Alex");
		firstNames.add("Jon");
		firstNames.add("Ronnie");
		firstNames.add("Bill");
		firstNames.add("Lloyd");
		firstNames.add("Tommy");
		firstNames.add("Leon");
		firstNames.add("Derek");
		firstNames.add("Warren");
		firstNames.add("Darrell");
		firstNames.add("Jerome");
		firstNames.add("Floyd");
		firstNames.add("Leo");
		firstNames.add("Alvin");
		firstNames.add("Tim");
		firstNames.add("Wesley");
		firstNames.add("Gordon");
		firstNames.add("Dean");
		firstNames.add("Greg");
		firstNames.add("Jorge");
		firstNames.add("Dustin");
		firstNames.add("Pedro");
		firstNames.add("Derrick");
		firstNames.add("Dan");
		firstNames.add("Lewis");
		firstNames.add("Zachary");
		firstNames.add("Corey");
		firstNames.add("Herman");
		firstNames.add("Maurice");
		firstNames.add("Vernon");
		firstNames.add("Roberto");
		firstNames.add("Clyde");
		firstNames.add("Glen");
		firstNames.add("Hector");
		firstNames.add("Shane");
		firstNames.add("Ricardo");
		firstNames.add("Sam");
		firstNames.add("Rick");
		firstNames.add("Lester");
		firstNames.add("Brent");
		firstNames.add("Ramon");
		firstNames.add("Charlie");
		firstNames.add("Tyler");
		firstNames.add("Gilbert");
		firstNames.add("Gene");
		firstNames.add("Marc");
		firstNames.add("Reginald");
		firstNames.add("Ruben");
		firstNames.add("Brett");
		firstNames.add("Angel");
		firstNames.add("Nathaniel");
		firstNames.add("Rafael");
		firstNames.add("Leslie");
		firstNames.add("Edgar");
		firstNames.add("Milton");
		firstNames.add("Raul");
		firstNames.add("Ben");
		firstNames.add("Chester");
		firstNames.add("Cecil");
		firstNames.add("Duane");
		firstNames.add("Franklin");
		firstNames.add("Andre");
		firstNames.add("Elmer");
		firstNames.add("Brad");
		firstNames.add("Gabriel");
		firstNames.add("Ron");
		firstNames.add("Mitchell");
		firstNames.add("Roland");
		firstNames.add("Arnold");
		firstNames.add("Harvey");
		firstNames.add("Jared");
		firstNames.add("Adrian");
		firstNames.add("Karl");
		firstNames.add("Cory");
		firstNames.add("Claude");
		firstNames.add("Erik");
		firstNames.add("Darryl");
		firstNames.add("Jamie");
		firstNames.add("Neil");
		firstNames.add("Jessie");
		firstNames.add("Christian");
		firstNames.add("Javier");
		firstNames.add("Fernando");
		firstNames.add("Clinton");
		firstNames.add("Ted");
		firstNames.add("Mathew");
		firstNames.add("Tyrone");
		firstNames.add("Darren");
		firstNames.add("Lonnie");
		firstNames.add("Lance");
		firstNames.add("Cody");
		firstNames.add("Julio");
		firstNames.add("Kelly");
		firstNames.add("Kurt");
		firstNames.add("Allan");
		firstNames.add("Nelson");
		firstNames.add("Guy");
		firstNames.add("Clayton");
		firstNames.add("Hugh");
		firstNames.add("Max");
		firstNames.add("Dwayne");
		firstNames.add("Dwight");
		firstNames.add("Armando");
		firstNames.add("Felix");
		firstNames.add("Jimmie");
		firstNames.add("Everett");
		firstNames.add("Jordan");
		firstNames.add("Ian");
		firstNames.add("Wallace");
		firstNames.add("Ken");
		firstNames.add("Bob");
		firstNames.add("Jaime");
		firstNames.add("Casey");
		firstNames.add("Alfredo");
		firstNames.add("Alberto");
		firstNames.add("Dave");
		firstNames.add("Ivan");
		firstNames.add("Johnnie");
		firstNames.add("Sidney");
		firstNames.add("Byron");
		firstNames.add("Julian");
		firstNames.add("Isaac");
		firstNames.add("Morris");
		firstNames.add("Clifton");
		firstNames.add("Willard");
		firstNames.add("Daryl");
		firstNames.add("Ross");
		firstNames.add("Virgil");
		firstNames.add("Andy");
		firstNames.add("Marshall");
		firstNames.add("Salvador");
		firstNames.add("Perry");
		firstNames.add("Kirk");
		firstNames.add("Sergio");
		firstNames.add("Marion");
		firstNames.add("Tracy");
		firstNames.add("Seth");
		firstNames.add("Kent");
		firstNames.add("Terrance");
		firstNames.add("Rene");
		firstNames.add("Eduardo");
		firstNames.add("Terrence");
		firstNames.add("Enrique");
		firstNames.add("Freddie");
		firstNames.add("Wade");


	}
	
	private void setLastNames(){
		lastNames = new ArrayList<String>();
		lastNames.add("Smith");
		lastNames.add("Johnson");
		lastNames.add("Williams");
		lastNames.add("Brown");
		lastNames.add("Jones");
		lastNames.add("Miller");
		lastNames.add("Davis");
		lastNames.add("Garcia");
		lastNames.add("Rodriguez");
		lastNames.add("Wilson");
		lastNames.add("Martinez");
		lastNames.add("Anderson");
		lastNames.add("Taylor");
		lastNames.add("Thomas");
		lastNames.add("Hernandez");
		lastNames.add("Moore");
		lastNames.add("Martin");
		lastNames.add("Jackson");
		lastNames.add("Thompson");
		lastNames.add("White");
		lastNames.add("Lopez");
		lastNames.add("Lee");
		lastNames.add("Gonzalez");
		lastNames.add("Harris");
		lastNames.add("Clark");
		lastNames.add("Lewis");
		lastNames.add("Robinson");
		lastNames.add("Walker");
		lastNames.add("Perez");
		lastNames.add("Hall");
		lastNames.add("Young");
		lastNames.add("Allen");
		lastNames.add("Sanchez");
		lastNames.add("Wright");
		lastNames.add("King");
		lastNames.add("Scott");
		lastNames.add("Green");
		lastNames.add("Baker");
		lastNames.add("Adams");
		lastNames.add("Nelson");
		lastNames.add("Hill");
		lastNames.add("Ramirez");
		lastNames.add("Campbell");
		lastNames.add("Mitchell");
		lastNames.add("Roberts");
		lastNames.add("Carter");
		lastNames.add("Phillips");
		lastNames.add("Evans");
		lastNames.add("Turner");
		lastNames.add("Torres");
		lastNames.add("Parker");
		lastNames.add("Collins");
		lastNames.add("Edwards");
		lastNames.add("Stewart");
		lastNames.add("Flores");
		lastNames.add("Morris");
		lastNames.add("Nguyen");
		lastNames.add("Murphy");
		lastNames.add("Rivera");
		lastNames.add("Cook");
		lastNames.add("Rogers");
		lastNames.add("Morgan");
		lastNames.add("Peterson");
		lastNames.add("Cooper");
		lastNames.add("Reed");
		lastNames.add("Bailey");
		lastNames.add("Bell");
		lastNames.add("Gomez");
		lastNames.add("Kelly");
		lastNames.add("Howard");
		lastNames.add("Ward");
		lastNames.add("Cox");
		lastNames.add("Diaz");
		lastNames.add("Richardson");
		lastNames.add("Wood");
		lastNames.add("Watson");
		lastNames.add("Brooks");
		lastNames.add("Bennett");
		lastNames.add("Gray");
		lastNames.add("James");
		lastNames.add("Reyes");
		lastNames.add("Cruz");
		lastNames.add("Hughes");
		lastNames.add("Price");
		lastNames.add("Myers");
		lastNames.add("Long");
		lastNames.add("Foster");
		lastNames.add("Sanders");
		lastNames.add("Ross");
		lastNames.add("Morales");
		lastNames.add("Powell");
		lastNames.add("Sullivan");
		lastNames.add("Russell");
		lastNames.add("Ortiz");
		lastNames.add("Jenkins");
		lastNames.add("Gutierrez");
		lastNames.add("Perry");
		lastNames.add("Butler");
		lastNames.add("Barnes");
		lastNames.add("Fisher");
		lastNames.add("Henderson");
		lastNames.add("Coleman");
		lastNames.add("Simmons");
		lastNames.add("Patterson");
		lastNames.add("Jordan");
		lastNames.add("Reynolds");
		lastNames.add("Hamilton");
		lastNames.add("Graham");
		lastNames.add("Kim");
		lastNames.add("Gonzales");
		lastNames.add("Alexander");
		lastNames.add("Ramos");
		lastNames.add("Wallace");
		lastNames.add("Griffin");
		lastNames.add("West");
		lastNames.add("Cole");
		lastNames.add("Hayes");
		lastNames.add("Chavez");
		lastNames.add("Gibson");
		lastNames.add("Bryant");
		lastNames.add("Ellis");
		lastNames.add("Stevens");
		lastNames.add("Murray");
		lastNames.add("Ford");
		lastNames.add("Marshall");
		lastNames.add("Owens");
		lastNames.add("Mcdonald");
		lastNames.add("Harrison");
		lastNames.add("Ruiz");
		lastNames.add("Kennedy");
		lastNames.add("Wells");
		lastNames.add("Alvarez");
		lastNames.add("Woods");
		lastNames.add("Mendoza");
		lastNames.add("Castillo");
		lastNames.add("Olson");
		lastNames.add("Webb");
		lastNames.add("Washington");
		lastNames.add("Tucker");
		lastNames.add("Freeman");
		lastNames.add("Burns");
		lastNames.add("Henry");
		lastNames.add("Vasquez");
		lastNames.add("Snyder");
		lastNames.add("Simpson");
		lastNames.add("Crawford");
		lastNames.add("Jimenez");
		lastNames.add("Porter");
		lastNames.add("Mason");
		lastNames.add("Shaw");
		lastNames.add("Gordon");
		lastNames.add("Wagner");
		lastNames.add("Hunter");
		lastNames.add("Romero");
		lastNames.add("Hicks");
		lastNames.add("Dixon");
		lastNames.add("Hunt");
		lastNames.add("Palmer");
		lastNames.add("Robertson");
		lastNames.add("Black");
		lastNames.add("Holmes");
		lastNames.add("Stone");
		lastNames.add("Meyer");
		lastNames.add("Boyd");
		lastNames.add("Mills");
		lastNames.add("Warren");
		lastNames.add("Fox");
		lastNames.add("Rose");
		lastNames.add("Rice");
		lastNames.add("Moreno");
		lastNames.add("Schmidt");
		lastNames.add("Patel");
		lastNames.add("Ferguson");
		lastNames.add("Nichols");
		lastNames.add("Herrera");
		lastNames.add("Medina");
		lastNames.add("Ryan");
		lastNames.add("Fernandez");
		lastNames.add("Weaver");
		lastNames.add("Daniels");
		lastNames.add("Stephens");
		lastNames.add("Gardner");
		lastNames.add("Payne");
		lastNames.add("Kelley");
		lastNames.add("Dunn");
		lastNames.add("Pierce");
		lastNames.add("Arnold");
		lastNames.add("Tran");
		lastNames.add("Spencer");
		lastNames.add("Peters");
		lastNames.add("Hawkins");
		lastNames.add("Grant");
		lastNames.add("Hansen");
		lastNames.add("Castro");
		lastNames.add("Hoffman");
		lastNames.add("Hart");
		lastNames.add("Elliott");
		lastNames.add("Cunningham");
		lastNames.add("Knight");
		lastNames.add("Bradley");
		lastNames.add("Carroll");
		lastNames.add("Hudson");
		lastNames.add("Duncan");
		lastNames.add("Armstrong");
		lastNames.add("Berry");
		lastNames.add("Andrews");
		lastNames.add("Johnston");
		lastNames.add("Ray");
		lastNames.add("Lane");
		lastNames.add("Riley");
		lastNames.add("Carpenter");
		lastNames.add("Perkins");
		lastNames.add("Aguilar");
		lastNames.add("Silva");
		lastNames.add("Richards");
		lastNames.add("Willis");
		lastNames.add("Matthews");
		lastNames.add("Chapman");
		lastNames.add("Lawrence");
		lastNames.add("Garza");
		lastNames.add("Vargas");
		lastNames.add("Watkins");
		lastNames.add("Wheeler");
		lastNames.add("Larson");
		lastNames.add("Carlson");
		lastNames.add("Harper");
		lastNames.add("George");
		lastNames.add("Greene");
		lastNames.add("Burke");
		lastNames.add("Guzman");
		lastNames.add("Morrison");
		lastNames.add("Munoz");
		lastNames.add("Jacobs");
		lastNames.add("Obrien");
		lastNames.add("Lawson");
		lastNames.add("Franklin");
		lastNames.add("Lynch");
		lastNames.add("Bishop");
		lastNames.add("Carr");
		lastNames.add("Salazar");
		lastNames.add("Austin");
		lastNames.add("Mendez");
		lastNames.add("Gilbert");
		lastNames.add("Jensen");
		lastNames.add("Williamson");
		lastNames.add("Montgomery");
		lastNames.add("Harvey");
		lastNames.add("Oliver");
		lastNames.add("Howell");
		lastNames.add("Dean");
		lastNames.add("Hanson");
		lastNames.add("Weber");
		lastNames.add("Garrett");
		lastNames.add("Sims");
		lastNames.add("Burton");
		lastNames.add("Fuller");
		lastNames.add("Soto");
		lastNames.add("Mccoy");
		lastNames.add("Welch");
		lastNames.add("Chen");
		lastNames.add("Schultz");
		lastNames.add("Walters");
		lastNames.add("Reid");
		lastNames.add("Fields");
		lastNames.add("Walsh");
		lastNames.add("Little");
		lastNames.add("Fowler");
		lastNames.add("Bowman");
		lastNames.add("Davidson");
		lastNames.add("May");
		lastNames.add("Day");
		lastNames.add("Schneider");
		lastNames.add("Newman");
		lastNames.add("Brewer");
		lastNames.add("Lucas");
		lastNames.add("Holland");
		lastNames.add("Wong");
		lastNames.add("Banks");
		lastNames.add("Santos");
		lastNames.add("Curtis");
		lastNames.add("Pearson");
		lastNames.add("Delgado");
		lastNames.add("Valdez");
		lastNames.add("Pena");
		lastNames.add("Rios");
		lastNames.add("Douglas");
		lastNames.add("Sandoval");
		lastNames.add("Barrett");
		lastNames.add("Hopkins");
		lastNames.add("Keller");
		lastNames.add("Guerrero");
		lastNames.add("Stanley");
		lastNames.add("Bates");
		lastNames.add("Alvarado");
		lastNames.add("Beck");
		lastNames.add("Ortega");
		lastNames.add("Wade");
		lastNames.add("Estrada");
		lastNames.add("Contreras");
		lastNames.add("Barnett");
		lastNames.add("Caldwell");
		lastNames.add("Santiago");
		lastNames.add("Lambert");
		lastNames.add("Powers");
		lastNames.add("Chambers");
		lastNames.add("Nunez");
		lastNames.add("Craig");
		lastNames.add("Leonard");
		lastNames.add("Lowe");
		lastNames.add("Rhodes");
		lastNames.add("Byrd");
		lastNames.add("Gregory");
		lastNames.add("Shelton");
		lastNames.add("Frazier");
		lastNames.add("Becker");
		lastNames.add("Maldonado");
		lastNames.add("Fleming");
		lastNames.add("Vega");
		lastNames.add("Sutton");
		lastNames.add("Cohen");
		lastNames.add("Jennings");
		lastNames.add("Parks");
		lastNames.add("Mcdaniel");
		lastNames.add("Watts");
		lastNames.add("Barker");
		lastNames.add("Norris");
		lastNames.add("Vaughn");
		lastNames.add("Vazquez");
		lastNames.add("Holt");
		lastNames.add("Schwartz");
		lastNames.add("Steele");
		lastNames.add("Benson");
		lastNames.add("Neal");
		lastNames.add("Dominguez");
		lastNames.add("Horton");
		lastNames.add("Terry");
		lastNames.add("Wolfe");
		lastNames.add("Hale");
		lastNames.add("Lyons");
		lastNames.add("Graves");
		lastNames.add("Haynes");
		lastNames.add("Miles");
		lastNames.add("Park");
		lastNames.add("Warner");
		lastNames.add("Padilla");
		lastNames.add("Bush");
		lastNames.add("Thornton");
		lastNames.add("Mccarthy");
		lastNames.add("Mann");
		lastNames.add("Zimmerman");
		lastNames.add("Erickson");
		lastNames.add("Fletcher");
		lastNames.add("Mckinney");
		lastNames.add("Page");
		lastNames.add("Dawson");
		lastNames.add("Joseph");
		lastNames.add("Marquez");
		lastNames.add("Reeves");
		lastNames.add("Klein");
		lastNames.add("Espinoza");
		lastNames.add("Baldwin");
		lastNames.add("Moran");
		lastNames.add("Love");
		lastNames.add("Robbins");
		lastNames.add("Higgins");
		lastNames.add("Ball");
		lastNames.add("Cortez");
		lastNames.add("Le");
		lastNames.add("Griffith");
		lastNames.add("Bowen");
		lastNames.add("Sharp");
		lastNames.add("Cummings");
		lastNames.add("Ramsey");
		lastNames.add("Hardy");
		lastNames.add("Swanson");
		lastNames.add("Barber");
		lastNames.add("Acosta");
		lastNames.add("Luna");
		lastNames.add("Chandler");
		lastNames.add("Daniel");
		lastNames.add("Blair");
		lastNames.add("Cross");
		lastNames.add("Simon");
		lastNames.add("Dennis");
		lastNames.add("Oconnor");
		lastNames.add("Quinn");
		lastNames.add("Gross");
		lastNames.add("Navarro");
		lastNames.add("Moss");
		lastNames.add("Fitzgerald");
		lastNames.add("Doyle");
		lastNames.add("Mclaughlin");
		lastNames.add("Rojas");
		lastNames.add("Rodgers");
		lastNames.add("Stevenson");
		lastNames.add("Singh");
		lastNames.add("Yang");
		lastNames.add("Figueroa");
		lastNames.add("Harmon");
		lastNames.add("Newton");
		lastNames.add("Paul");
		lastNames.add("Manning");
		lastNames.add("Garner");
		lastNames.add("Mcgee");
		lastNames.add("Reese");
		lastNames.add("Francis");
		lastNames.add("Burgess");
		lastNames.add("Adkins");
		lastNames.add("Goodman");
		lastNames.add("Curry");
		lastNames.add("Brady");
		lastNames.add("Christensen");
		lastNames.add("Potter");
		lastNames.add("Walton");
		lastNames.add("Goodwin");
		lastNames.add("Mullins");
		lastNames.add("Molina");
		lastNames.add("Webster");
		lastNames.add("Fischer");
		lastNames.add("Campos");
		lastNames.add("Avila");
		lastNames.add("Sherman");
		lastNames.add("Todd");
		lastNames.add("Chang");
		lastNames.add("Blake");
		lastNames.add("Malone");
		lastNames.add("Wolf");
		lastNames.add("Hodges");
		lastNames.add("Juarez");
		lastNames.add("Gill");
		lastNames.add("Farmer");
		lastNames.add("Hines");
		lastNames.add("Gallagher");
		lastNames.add("Duran");
		lastNames.add("Hubbard");
		lastNames.add("Cannon");
		lastNames.add("Miranda");
		lastNames.add("Wang");
		lastNames.add("Saunders");
		lastNames.add("Tate");
		lastNames.add("Mack");
		lastNames.add("Hammond");
		lastNames.add("Carrillo");
		lastNames.add("Townsend");
		lastNames.add("Wise");
		lastNames.add("Ingram");
		lastNames.add("Barton");
		lastNames.add("Mejia");
		lastNames.add("Ayala");
		lastNames.add("Schroeder");
		lastNames.add("Hampton");
		lastNames.add("Rowe");
		lastNames.add("Parsons");
		lastNames.add("Frank");
		lastNames.add("Waters");
		lastNames.add("Strickland");
		lastNames.add("Osborne");
		lastNames.add("Maxwell");
		lastNames.add("Chan");
		lastNames.add("Deleon");
		lastNames.add("Norman");
		lastNames.add("Harrington");
		lastNames.add("Casey");
		lastNames.add("Patton");
		lastNames.add("Logan");
		lastNames.add("Bowers");
		lastNames.add("Mueller");
		lastNames.add("Glover");
		lastNames.add("Floyd");
		lastNames.add("Hartman");
		lastNames.add("Buchanan");
		lastNames.add("Cobb");
		lastNames.add("French");
		lastNames.add("Kramer");
		lastNames.add("Mccormick");
		lastNames.add("Clarke");
		lastNames.add("Tyler");
		lastNames.add("Gibbs");
		lastNames.add("Moody");
		lastNames.add("Conner");
		lastNames.add("Sparks");
		lastNames.add("Mcguire");
		lastNames.add("Leon");
		lastNames.add("Bauer");
		lastNames.add("Norton");
		lastNames.add("Pope");
		lastNames.add("Flynn");
		lastNames.add("Hogan");
		lastNames.add("Robles");
		lastNames.add("Salinas");
		lastNames.add("Yates");
		lastNames.add("Lindsey");
		lastNames.add("Lloyd");
		lastNames.add("Marsh");
		lastNames.add("Mcbride");
		lastNames.add("Owen");
		lastNames.add("Solis");
		lastNames.add("Pham");
		lastNames.add("Lang");
		lastNames.add("Pratt");
		lastNames.add("Lara");
		lastNames.add("Brock");
		lastNames.add("Ballard");
		lastNames.add("Trujillo");
		lastNames.add("Shaffer");
		lastNames.add("Drake");
		lastNames.add("Roman");
		lastNames.add("Aguirre");
		lastNames.add("Morton");
		lastNames.add("Stokes");
		lastNames.add("Lamb");
		lastNames.add("Pacheco");
		lastNames.add("Patrick");
		lastNames.add("Cochran");
		lastNames.add("Shepherd");
		lastNames.add("Cain");
		lastNames.add("Burnett");
		lastNames.add("Hess");
		lastNames.add("Li");
		lastNames.add("Cervantes");
		lastNames.add("Olsen");
		lastNames.add("Briggs");
		lastNames.add("Ochoa");
		lastNames.add("Cabrera");
		lastNames.add("Velasquez");
		lastNames.add("Montoya");
		lastNames.add("Roth");
		lastNames.add("Meyers");
		lastNames.add("Cardenas");
		lastNames.add("Fuentes");
		lastNames.add("Weiss");
		lastNames.add("Wilkins");
		lastNames.add("Hoover");
		lastNames.add("Nicholson");
		lastNames.add("Underwood");
		lastNames.add("Short");
		lastNames.add("Carson");
		lastNames.add("Morrow");
		lastNames.add("Colon");
		lastNames.add("Holloway");
		lastNames.add("Summers");
		lastNames.add("Bryan");
		lastNames.add("Petersen");
		lastNames.add("Mckenzie");
		lastNames.add("Serrano");
		lastNames.add("Wilcox");
		lastNames.add("Carey");
		lastNames.add("Clayton");
		lastNames.add("Poole");
		lastNames.add("Calderon");
		lastNames.add("Gallegos");
		lastNames.add("Greer");
		lastNames.add("Rivas");
		lastNames.add("Guerra");
		lastNames.add("Decker");
		lastNames.add("Collier");
		lastNames.add("Wall");
		lastNames.add("Whitaker");
		lastNames.add("Bass");
		lastNames.add("Flowers");
		lastNames.add("Davenport");
		lastNames.add("Conley");
		lastNames.add("Houston");
		lastNames.add("Huff");
		lastNames.add("Copeland");
		lastNames.add("Hood");
		lastNames.add("Monroe");
		lastNames.add("Massey");
		lastNames.add("Roberson");
		lastNames.add("Combs");
		lastNames.add("Franco");
		lastNames.add("Larsen");
		lastNames.add("Pittman");
		lastNames.add("Randall");
		lastNames.add("Skinner");
		lastNames.add("Wilkinson");
		lastNames.add("Kirby");
		lastNames.add("Cameron");
		lastNames.add("Bridges");
		lastNames.add("Anthony");
		lastNames.add("Richard");
		lastNames.add("Kirk");
		lastNames.add("Bruce");
		lastNames.add("Singleton");
		lastNames.add("Mathis");
		lastNames.add("Bradford");
		lastNames.add("Boone");
		lastNames.add("Abbott");
		lastNames.add("Charles");
		lastNames.add("Allison");
		lastNames.add("Sweeney");
		lastNames.add("Atkinson");
		lastNames.add("Horn");
		lastNames.add("Jefferson");
		lastNames.add("Rosales");
		lastNames.add("York");
		lastNames.add("Christian");
		lastNames.add("Phelps");
		lastNames.add("Farrell");
		lastNames.add("Castaneda");
		lastNames.add("Nash");
		lastNames.add("Dickerson");
		lastNames.add("Bond");
		lastNames.add("Wyatt");
		lastNames.add("Foley");
		lastNames.add("Chase");
		lastNames.add("Gates");
		lastNames.add("Vincent");
		lastNames.add("Mathews");
		lastNames.add("Hodge");
		lastNames.add("Garrison");
		lastNames.add("Trevino");
		lastNames.add("Villarreal");
		lastNames.add("Heath");
		lastNames.add("Dalton");
		lastNames.add("Valencia");
		lastNames.add("Callahan");
		lastNames.add("Hensley");
		lastNames.add("Atkins");
		lastNames.add("Huffman");
		lastNames.add("Roy");
		lastNames.add("Boyer");
		lastNames.add("Shields");
		lastNames.add("Lin");
		lastNames.add("Hancock");
		lastNames.add("Grimes");
		lastNames.add("Glenn");
		lastNames.add("Cline");
		lastNames.add("Delacruz");
		lastNames.add("Camacho");
		lastNames.add("Dillon");
		lastNames.add("Parrish");
		lastNames.add("Oneill");
		lastNames.add("Melton");
		lastNames.add("Booth");
		lastNames.add("Kane");
		lastNames.add("Berg");
		lastNames.add("Harrell");
		lastNames.add("Pitts");
		lastNames.add("Savage");
		lastNames.add("Wiggins");
		lastNames.add("Brennan");
		lastNames.add("Salas");
		lastNames.add("Marks");
		lastNames.add("Russo");
		lastNames.add("Sawyer");
		lastNames.add("Baxter");
		lastNames.add("Golden");
		lastNames.add("Hutchinson");
		lastNames.add("Liu");
		lastNames.add("Walter");
		lastNames.add("Mcdowell");
		lastNames.add("Wiley");
		lastNames.add("Rich");
		lastNames.add("Humphrey");
		lastNames.add("Johns");
		lastNames.add("Koch");
		lastNames.add("Suarez");
		lastNames.add("Hobbs");
		lastNames.add("Beard");
		lastNames.add("Gilmore");
		lastNames.add("Ibarra");
		lastNames.add("Keith");
		lastNames.add("Macias");
		lastNames.add("Khan");
		lastNames.add("Andrade");
		lastNames.add("Ware");
		lastNames.add("Stephenson");
		lastNames.add("Henson");
		lastNames.add("Wilkerson");
		lastNames.add("Dyer");
		lastNames.add("Mcclure");
		lastNames.add("Blackwell");
		lastNames.add("Mercado");
		lastNames.add("Tanner");
		lastNames.add("Eaton");
		lastNames.add("Clay");
		lastNames.add("Barron");
		lastNames.add("Beasley");
		lastNames.add("Oneal");
		lastNames.add("Small");
		lastNames.add("Preston");
		lastNames.add("Wu");
		lastNames.add("Zamora");
		lastNames.add("Macdonald");
		lastNames.add("Vance");
		lastNames.add("Snow");
		lastNames.add("Mcclain");
		lastNames.add("Stafford");
		lastNames.add("Orozco");
		lastNames.add("Barry");
		lastNames.add("English");
		lastNames.add("Shannon");
		lastNames.add("Kline");
		lastNames.add("Jacobson");
		lastNames.add("Woodard");
		lastNames.add("Huang");
		lastNames.add("Kemp");
		lastNames.add("Mosley");
		lastNames.add("Prince");
		lastNames.add("Merritt");
		lastNames.add("Hurst");
		lastNames.add("Villanueva");
		lastNames.add("Roach");
		lastNames.add("Nolan");
		lastNames.add("Lam");
		lastNames.add("Yoder");
		lastNames.add("Mccullough");
		lastNames.add("Lester");
		lastNames.add("Santana");
		lastNames.add("Valenzuela");
		lastNames.add("Winters");
		lastNames.add("Barrera");
		lastNames.add("Orr");
		lastNames.add("Leach");
		lastNames.add("Berger");
		lastNames.add("Mckee");
		lastNames.add("Strong");
		lastNames.add("Conway");
		lastNames.add("Stein");
		lastNames.add("Whitehead");
		lastNames.add("Bullock");
		lastNames.add("Escobar");
		lastNames.add("Knox");
		lastNames.add("Meadows");
		lastNames.add("Solomon");
		lastNames.add("Velez");
		lastNames.add("Odonnell");
		lastNames.add("Kerr");
		lastNames.add("Stout");
		lastNames.add("Blankenship");
		lastNames.add("Browning");
		lastNames.add("Kent");
		lastNames.add("Lozano");
		lastNames.add("Bartlett");
		lastNames.add("Pruitt");
		lastNames.add("Buck");
		lastNames.add("Barr");
		lastNames.add("Gaines");
		lastNames.add("Durham");
		lastNames.add("Gentry");
		lastNames.add("Mcintyre");
		lastNames.add("Sloan");
		lastNames.add("Rocha");
		lastNames.add("Melendez");
		lastNames.add("Herman");
		lastNames.add("Sexton");
		lastNames.add("Moon");
		lastNames.add("Hendricks");
		lastNames.add("Rangel");
		lastNames.add("Stark");
		lastNames.add("Lowery");
		lastNames.add("Hardin");
		lastNames.add("Hull");
		lastNames.add("Sellers");
		lastNames.add("Ellison");
		lastNames.add("Calhoun");
		lastNames.add("Gillespie");
		lastNames.add("Mora");
		lastNames.add("Knapp");
		lastNames.add("Mccall");
		lastNames.add("Morse");
		lastNames.add("Dorsey");
		lastNames.add("Weeks");
		lastNames.add("Nielsen");
		lastNames.add("Livingston");
		lastNames.add("Leblanc");
		lastNames.add("Mclean");
		lastNames.add("Bradshaw");
		lastNames.add("Glass");
		lastNames.add("Middleton");
		lastNames.add("Buckley");
		lastNames.add("Schaefer");
		lastNames.add("Frost");
		lastNames.add("Howe");
		lastNames.add("House");
		lastNames.add("Mcintosh");
		lastNames.add("Ho");
		lastNames.add("Pennington");
		lastNames.add("Reilly");
		lastNames.add("Hebert");
		lastNames.add("Mcfarland");
		lastNames.add("Hickman");
		lastNames.add("Noble");
		lastNames.add("Spears");
		lastNames.add("Conrad");
		lastNames.add("Arias");
		lastNames.add("Galvan");
		lastNames.add("Velazquez");
		lastNames.add("Huynh");
		lastNames.add("Frederick");
		lastNames.add("Randolph");
		lastNames.add("Cantu");
		lastNames.add("Fitzpatrick");
		lastNames.add("Mahoney");
		lastNames.add("Peck");
		lastNames.add("Villa");
		lastNames.add("Michael");
		lastNames.add("Donovan");
		lastNames.add("Mcconnell");
		lastNames.add("Walls");
		lastNames.add("Boyle");
		lastNames.add("Mayer");
		lastNames.add("Zuniga");
		lastNames.add("Giles");
		lastNames.add("Pineda");
		lastNames.add("Pace");
		lastNames.add("Hurley");
		lastNames.add("Mays");
		lastNames.add("Mcmillan");
		lastNames.add("Crosby");
		lastNames.add("Ayers");
		lastNames.add("Case");
		lastNames.add("Bentley");
		lastNames.add("Shepard");
		lastNames.add("Everett");
		lastNames.add("Pugh");
		lastNames.add("David");
		lastNames.add("Mcmahon");
		lastNames.add("Dunlap");
		lastNames.add("Bender");
		lastNames.add("Hahn");
		lastNames.add("Harding");
		lastNames.add("Acevedo");
		lastNames.add("Raymond");
		lastNames.add("Blackburn");
		lastNames.add("Duffy");
		lastNames.add("Landry");
		lastNames.add("Dougherty");
		lastNames.add("Bautista");
		lastNames.add("Shah");
		lastNames.add("Potts");
		lastNames.add("Arroyo");
		lastNames.add("Valentine");
		lastNames.add("Meza");
		lastNames.add("Gould");
		lastNames.add("Vaughan");
		lastNames.add("Fry");
		lastNames.add("Rush");
		lastNames.add("Avery");
		lastNames.add("Herring");
		lastNames.add("Dodson");
		lastNames.add("Clements");
		lastNames.add("Sampson");
		lastNames.add("Tapia");
		lastNames.add("Bean");
		lastNames.add("Lynn");
		lastNames.add("Crane");
		lastNames.add("Farley");
		lastNames.add("Cisneros");
		lastNames.add("Benton");
		lastNames.add("Ashley");
		lastNames.add("Mckay");
		lastNames.add("Finley");
		lastNames.add("Best");
		lastNames.add("Blevins");
		lastNames.add("Friedman");
		lastNames.add("Moses");
		lastNames.add("Sosa");
		lastNames.add("Blanchard");
		lastNames.add("Huber");
		lastNames.add("Frye");
		lastNames.add("Krueger");
		lastNames.add("Bernard");
		lastNames.add("Rosario");
		lastNames.add("Rubio");
		lastNames.add("Mullen");
		lastNames.add("Benjamin");
		lastNames.add("Haley");
		lastNames.add("Chung");
		lastNames.add("Moyer");
		lastNames.add("Choi");
		lastNames.add("Horne");
		lastNames.add("Yu");
		lastNames.add("Woodward");
		lastNames.add("Ali");
		lastNames.add("Nixon");
		lastNames.add("Hayden");
		lastNames.add("Rivers");
		lastNames.add("Estes");
		lastNames.add("Mccarty");
		lastNames.add("Richmond");
		lastNames.add("Stuart");
		lastNames.add("Maynard");
		lastNames.add("Brandt");
		lastNames.add("Oconnell");
		lastNames.add("Hanna");
		lastNames.add("Sanford");
		lastNames.add("Sheppard");
		lastNames.add("Church");
		lastNames.add("Burch");
		lastNames.add("Levy");
		lastNames.add("Rasmussen");
		lastNames.add("Coffey");
		lastNames.add("Ponce");
		lastNames.add("Faulkner");
		lastNames.add("Donaldson");
		lastNames.add("Schmitt");
		lastNames.add("Novak");
		lastNames.add("Costa");
		lastNames.add("Montes");
		lastNames.add("Booker");
		lastNames.add("Cordova");
		lastNames.add("Waller");
		lastNames.add("Arellano");
		lastNames.add("Maddox");
		lastNames.add("Mata");
		lastNames.add("Bonilla");
		lastNames.add("Stanton");
		lastNames.add("Compton");
		lastNames.add("Kaufman");
		lastNames.add("Dudley");
		lastNames.add("Mcpherson");
		lastNames.add("Beltran");
		lastNames.add("Dickson");
		lastNames.add("Mccann");
		lastNames.add("Villegas");
		lastNames.add("Proctor");
		lastNames.add("Hester");
		lastNames.add("Cantrell");
		lastNames.add("Daugherty");
		lastNames.add("Cherry");
		lastNames.add("Bray");
		lastNames.add("Davila");
		lastNames.add("Rowland");
		lastNames.add("Madden");
		lastNames.add("Levine");
		lastNames.add("Spence");
		lastNames.add("Good");
		lastNames.add("Irwin");
		lastNames.add("Werner");
		lastNames.add("Krause");
		lastNames.add("Petty");
		lastNames.add("Whitney");
		lastNames.add("Baird");
		lastNames.add("Hooper");
		lastNames.add("Pollard");
		lastNames.add("Zavala");
		lastNames.add("Jarvis");
		lastNames.add("Holden");
		lastNames.add("Hendrix");
		lastNames.add("Haas");
		lastNames.add("Mcgrath");
		lastNames.add("Bird");
		lastNames.add("Lucero");
		lastNames.add("Terrell");
		lastNames.add("Riggs");
		lastNames.add("Joyce");
		lastNames.add("Rollins");
		lastNames.add("Mercer");
		lastNames.add("Galloway");
		lastNames.add("Duke");
		lastNames.add("Odom");
		lastNames.add("Andersen");
		lastNames.add("Downs");
		lastNames.add("Hatfield");
		lastNames.add("Benitez");
		lastNames.add("Archer");
		lastNames.add("Huerta");
		lastNames.add("Travis");
		lastNames.add("Mcneil");
		lastNames.add("Hinton");
		lastNames.add("Zhang");
		lastNames.add("Hays");
		lastNames.add("Mayo");
		lastNames.add("Fritz");
		lastNames.add("Branch");
		lastNames.add("Mooney");
		lastNames.add("Ewing");
		lastNames.add("Ritter");
		lastNames.add("Esparza");
		lastNames.add("Frey");
		lastNames.add("Braun");
		lastNames.add("Gay");
		lastNames.add("Riddle");
		lastNames.add("Haney");
		lastNames.add("Kaiser");
		lastNames.add("Holder");
		lastNames.add("Chaney");
		lastNames.add("Mcknight");
		lastNames.add("Gamble");
		lastNames.add("Vang");
		lastNames.add("Cooley");
		lastNames.add("Carney");
		lastNames.add("Cowan");
		lastNames.add("Forbes");
		lastNames.add("Ferrell");
		lastNames.add("Davies");
		lastNames.add("Barajas");
		lastNames.add("Shea");
		lastNames.add("Osborn");
		lastNames.add("Bright");
		lastNames.add("Cuevas");
		lastNames.add("Bolton");
		lastNames.add("Murillo");
		lastNames.add("Lutz");
		lastNames.add("Duarte");
		lastNames.add("Kidd");
		lastNames.add("Key");
		lastNames.add("Cooke");


	}
	
	private void setJobCities(){
		
		cities = new ArrayList<DummyCity>();
		
		cities.add(new DummyCity("Minneapolis",407207,12,"MN",44.98,-93.2636111));
//		cities.add(new DummyCity("Saint Paul",297640,9,"MN",44.9444444,-93.0930556));
//		cities.add(new DummyCity("Bloomington",86314,3,"MN",44.8408333,-93.2980556));
//		cities.add(new DummyCity("Brooklyn Park",78728,3,"MN",45.0941667,-93.3561111));
//		cities.add(new DummyCity("Plymouth",75057,3,"MN",45.0105556,-93.4552778));
//		cities.add(new DummyCity("Maple Grove",66945,2,"MN",45.0725,-93.4555556));
//		cities.add(new DummyCity("Woodbury",66807,2,"MN",44.9238889,-92.9591667));
//		cities.add(new DummyCity("Eagan",66084,2,"MN",44.8041667,-93.1666667));
//		cities.add(new DummyCity("Eden Prairie",63228,2,"MN",44.8547222,-93.4705556));
//		cities.add(new DummyCity("Coon Rapids",62112,2,"MN",45.12,-93.2875));
//		cities.add(new DummyCity("Burnsville",61630,2,"MN",44.7677778,-93.2775));
//		cities.add(new DummyCity("Blaine",61190,2,"MN",45.1608333,-93.2347222));
//		cities.add(new DummyCity("Lakeville",59866,2,"MN",44.6497222,-93.2425));
//		cities.add(new DummyCity("Minnetonka",51486,2,"MN",44.9133333,-93.5030556));
//		cities.add(new DummyCity("Apple Valley",50487,2,"MN",44.7319444,-93.2175));
//		cities.add(new DummyCity("Edina",49597,2,"MN",44.8897222,-93.3497222));
//		cities.add(new DummyCity("Saint Louis Park",47502,2,"MN",44.9483333,-93.3477778));
//		cities.add(new DummyCity("Maplewood",40199,2,"MN",44.9530556,-92.995));
//		cities.add(new DummyCity("Shakopee",39677,2,"MN",44.7980556,-93.5266667));
//		cities.add(new DummyCity("Richfield",36179,1,"MN",44.8833333,-93.2827778));
//		cities.add(new DummyCity("Cottage Grove",35630,1,"MN",44.8277778,-92.9436111));
//		cities.add(new DummyCity("Roseville",35319,1,"MN",45.0061111,-93.1563889));
//		cities.add(new DummyCity("Inver Grove Heights",34709,1,"MN",44.8480556,-93.0425));
//		cities.add(new DummyCity("Andover",32006,1,"MN",45.2333333,-93.2911111));
//		cities.add(new DummyCity("Brooklyn Center",30729,1,"MN",45.0761111,-93.3325));
//		cities.add(new DummyCity("Savage",29208,1,"MN",44.7791667,-93.3361111));
//		cities.add(new DummyCity("Oakdale",28033,1,"MN",44.9630556,-92.9647222));
//		cities.add(new DummyCity("Fridley",27670,1,"MN",45.0861111,-93.2630556));
//		cities.add(new DummyCity("Shoreview",26194,1,"MN",45.0791667,-93.1469444));
//		cities.add(new DummyCity("Ramsey",25598,1,"MN",45.2461111,-93.4519444));
//		cities.add(new DummyCity("Prior Lake",25039,1,"MN",44.7133333,-93.4225));
//		cities.add(new DummyCity("White Bear Lake",24986,1,"MN",45.0847222,-93.0097222));
//		cities.add(new DummyCity("Chanhassen",24967,1,"MN",44.8622222,-93.5305556));
//		cities.add(new DummyCity("Chaska",24838,1,"MN",44.7894444,-93.6019444));
//		cities.add(new DummyCity("Champlin",23828,1,"MN",45.1888889,-93.3972222));
//		cities.add(new DummyCity("Elk River",23746,1,"MN",45.3038889,-93.5669444));
//		cities.add(new DummyCity("Rosemount",23008,1,"MN",44.7394444,-93.1255556));
//		cities.add(new DummyCity("Crystal",22605,1,"MN",45.0327778,-93.36));
//		cities.add(new DummyCity("Farmington",22571,1,"MN",44.6402778,-93.1433333));
//		cities.add(new DummyCity("Hastings",22566,1,"MN",44.7433333,-92.8522222));
//		cities.add(new DummyCity("New Brighton",22266,1,"MN",45.0655556,-93.2016667));
//		cities.add(new DummyCity("Lino Lakes",20948,1,"MN",45.1602778,-93.0886111));
//		cities.add(new DummyCity("Golden Valley",20866,1,"MN",45.0097222,-93.3488889));
//		cities.add(new DummyCity("New Hope",20792,1,"MN",45.0380556,-93.3863889));
//		cities.add(new DummyCity("South Saint Paul",20487,1,"MN",44.8927778,-93.0347222));
//		cities.add(new DummyCity("West Saint Paul",19802,1,"MN",44.9161111,-93.1013889));
//		cities.add(new DummyCity("Columbia Heights",19775,1,"MN",45.0408333,-93.2627778));
//		cities.add(new DummyCity("Forest Lake",19399,1,"MN",45.2788889,-92.985));
//		cities.add(new DummyCity("Stillwater",18800,1,"MN",45.0563889,-92.8058333));
//		cities.add(new DummyCity("Hopkins",18056,1,"MN",44.925,-93.4625));
//		cities.add(new DummyCity("Anoka",17276,1,"MN",45.1977778,-93.3869444));
//		cities.add(new DummyCity("Saint Michael",17267,1,"MN",45.21,-93.6647222));
//		cities.add(new DummyCity("Buffalo",16312,1,"MN",45.1719444,-93.8744444));
//		cities.add(new DummyCity("Ham Lake",15888,1,"MN",45.2502778,-93.2497222));
//		cities.add(new DummyCity("Otsego",15047,1,"MN",45.2741667,-93.5911111));
//		cities.add(new DummyCity("Robbinsdale",14320,1,"MN",45.0322222,-93.3383333));
//		cities.add(new DummyCity("Hugo",14239,1,"MN",45.16,-92.9930556));
//		cities.add(new DummyCity("Monticello",13236,1,"MN",45.3055556,-93.7938889));
//		cities.add(new DummyCity("Vadnais Heights",13143,1,"MN",45.0575,-93.0736111));
//		cities.add(new DummyCity("Mounds View",12676,1,"MN",45.105,-93.2083333));
//		cities.add(new DummyCity("North Saint Paul",12224,1,"MN",45.0125,-92.9916667));
//		cities.add(new DummyCity("East Bethel",11643,1,"MN",45.3194444,-93.2022222));
//		cities.add(new DummyCity("Rogers",12393,1,"MN",45.1888889,-93.5527778));
//		cities.add(new DummyCity("Waconia",11776,1,"MN",44.8508333,-93.7866667));
//		cities.add(new DummyCity("Mendota Heights",11222,1,"MN",44.8836111,-93.1380556));
//		cities.add(new DummyCity("Big Lake",10660,1,"MN",45.3325,-93.7458333));
//		cities.add(new DummyCity("Little Canada",10329,1,"MN",45.0269444,-93.0875));
//		cities.add(new DummyCity("North Branch",10160,1,"MN",45.5113889,-92.98));
//		cities.add(new DummyCity("Arden Hills",10000,1,"MN",45.0502778,-93.1563889));
//		cities.add(new DummyCity("Mound",10000,1,"MN",44.9366667,-93.6658333));
//		cities.add(new DummyCity("Saint Anthony",10000,1,"MN",45.0205556,-93.2177778));
//		cities.add(new DummyCity("Cambridge",10000,1,"MN",45.5727778,-93.2241667));
//		cities.add(new DummyCity("Oak Grove",10000,1,"MN",45.3408333,-93.3266667));
//		cities.add(new DummyCity("Lake Elmo",10000,1,"MN",44.9958333,-92.8791667));
//		cities.add(new DummyCity("Victoria",10000,1,"MN",44.8586111,-93.6613889));
//		cities.add(new DummyCity("Mahtomedi",10000,1,"MN",45.0697222,-92.9513889));
//		cities.add(new DummyCity("Orono",10000,1,"MN",44.9713889,-93.6041667));
//		cities.add(new DummyCity("Wyoming",10000,1,"MN",45.3363889,-92.9969444));
//		cities.add(new DummyCity("Shorewood",10000,1,"MN",44.9008333,-93.5888889));
//		cities.add(new DummyCity("New Prague",10000,1,"MN",44.5433333,-93.5758333));
//		cities.add(new DummyCity("Saint Francis",10000,1,"MN",45.3869444,-93.3591667));
//		cities.add(new DummyCity("Albertville",10000,1,"MN",45.2377778,-93.6541667));
//		cities.add(new DummyCity("Belle Plaine",10000,1,"MN",44.6227778,-93.7683333));
//		cities.add(new DummyCity("Minnetrista",10000,1,"MN",44.9383333,-93.7175));
//		cities.add(new DummyCity("Spring Lake Park",10000,1,"MN",45.1077778,-93.2377778));
//		cities.add(new DummyCity("Jordan",8000,1,"MN",44.6669444,-93.6266667));
//		cities.add(new DummyCity("Delano",8000,1,"MN",45.0419444,-93.7888889));
//		cities.add(new DummyCity("Corcoran",8000,1,"MN",45.0952778,-93.5472222));
//		cities.add(new DummyCity("Falcon Heights",8000,1,"MN",44.9916667,-93.1661111));
//		cities.add(new DummyCity("Isanti",8000,1,"MN",45.4902778,-93.2475));
//		cities.add(new DummyCity("Saint Paul Park",8000,1,"MN",44.8422222,-92.9911111));
//		cities.add(new DummyCity("Medina",8000,1,"MN",45.0352778,-93.5822222));
//		cities.add(new DummyCity("Circle Pines",8000,1,"MN",45.1486111,-93.1513889));
//		cities.add(new DummyCity("Chisago City",8000,1,"MN",45.3736111,-92.8897222));
//		cities.add(new DummyCity("Dayton",8000,1,"MN",45.2438889,-93.5147222));
//		cities.add(new DummyCity("Oak Park Heights",8000,1,"MN",45.0313889,-92.7927778));
//		cities.add(new DummyCity("Princeton",8000,1,"MN",45.57,-93.5813889));
//		cities.add(new DummyCity("North Oaks",8000,1,"MN",45.1027778,-93.0788889));
//		cities.add(new DummyCity("Becker",8000,1,"MN",45.3933333,-93.8766667));
//		cities.add(new DummyCity("Nowthen",8000,1,"MN",45.3280556,-93.47));
//		cities.add(new DummyCity("Lindstrom",8000,1,"MN",45.3894444,-92.8477778));
//		cities.add(new DummyCity("Rockford",8000,1,"MN",45.0883333,-93.7341667));
//		cities.add(new DummyCity("Watertown",8000,1,"MN",44.9636111,-93.8469444));
//		cities.add(new DummyCity("Wayzata",8000,1,"MN",44.9741667,-93.5063889));
//		cities.add(new DummyCity("Carver",8000,1,"MN",44.7636111,-93.6255556));
//		cities.add(new DummyCity("Grant",8000,1,"MN",45.0844444,-92.9102778));
//		cities.add(new DummyCity("Le Sueur",8000,1,"MN",44.4613889,-93.915));
//		cities.add(new DummyCity("Scandia",8000,1,"MN",45.2536111,-92.8055556));
//		cities.add(new DummyCity("Centerville",8000,1,"MN",45.1630556,-93.0555556));
//		cities.add(new DummyCity("Deephaven",8000,1,"MN",44.9297222,-93.5222222));
//		cities.add(new DummyCity("Bayport",8000,1,"MN",45.0213889,-92.7808333));
//		cities.add(new DummyCity("Independence",8000,1,"MN",45.0252778,-93.7072222));
//		cities.add(new DummyCity("Norwood Young America",8000,1,"MN",44.7736111,-93.9213889));
//		cities.add(new DummyCity("Newport",8000,1,"MN",44.8663889,-93.0002778));
//		cities.add(new DummyCity("Annandale",8000,1,"MN",45.2627778,-94.1241667));
//		cities.add(new DummyCity("Hanover",8000,1,"MN",45.1558333,-93.6661111));
//		cities.add(new DummyCity("Montrose",8000,1,"MN",45.065,-93.9108333));
//		cities.add(new DummyCity("Montgomery",8000,1,"MN",44.4388889,-93.5811111));
//		cities.add(new DummyCity("Afton",8000,1,"MN",44.9027778,-92.7833333));
//		cities.add(new DummyCity("Milaca",8000,1,"MN",45.7558333,-93.6541667));
//		cities.add(new DummyCity("Greenfield",8000,1,"MN",45.1033333,-93.6911111));
//		cities.add(new DummyCity("Cokato",8000,1,"MN",45.0758333,-94.1897222));
//		cities.add(new DummyCity("Le Center",8000,1,"MN",44.3894444,-93.73));
//		cities.add(new DummyCity("Osseo",8000,1,"MN",45.1194444,-93.4022222));
//		cities.add(new DummyCity("Lauderdale",8000,1,"MN",44.9986111,-93.2055556));
//		cities.add(new DummyCity("Saint Bonifacius",8000,1,"MN",44.9055556,-93.7472222));
//		cities.add(new DummyCity("Gaylord",8000,1,"MN",44.5530556,-94.2202778));
//		cities.add(new DummyCity("Excelsior",8000,1,"MN",44.9033333,-93.5661111));
//		cities.add(new DummyCity("Arlington",8000,1,"MN",44.6083333,-94.0802778));
//		cities.add(new DummyCity("Maple Lake",8000,1,"MN",45.2291667,-94.0016667));
//		cities.add(new DummyCity("Lexington",8000,1,"MN",45.1425,-93.1630556));
//		cities.add(new DummyCity("Howard Lake",8000,1,"MN",45.0608333,-94.0730556));
//		cities.add(new DummyCity("Waterville",8000,1,"MN",44.2188889,-93.5677778));
//		cities.add(new DummyCity("Mayer",8000,1,"MN",44.885,-93.8875));
//		cities.add(new DummyCity("Lakeland",8000,1,"MN",44.9563889,-92.7655556));
//		cities.add(new DummyCity("Maple Plain",8000,1,"MN",45.0072222,-93.6555556));
//		cities.add(new DummyCity("Long Lake",8000,1,"MN",44.9866667,-93.5713889));
//		cities.add(new DummyCity("Braham",8000,1,"MN",45.7227778,-93.1705556));
//		cities.add(new DummyCity("Clearwater",8000,1,"MN",45.4194444,-94.0486111));
//		cities.add(new DummyCity("Spring Park",8000,1,"MN",44.9352778,-93.6319444));
//		cities.add(new DummyCity("Cologne",8000,1,"MN",44.7716667,-93.7811111));
//		cities.add(new DummyCity("Tonka Bay",8000,1,"MN",44.9086111,-93.5927778));
//		cities.add(new DummyCity("Stacy",8000,1,"MN",45.3980556,-92.9872222));
//		cities.add(new DummyCity("Winthrop",8000,1,"MN",44.5430556,-94.3661111));
//		cities.add(new DummyCity("Waverly",8000,1,"MN",45.0666667,-93.9661111));
//		cities.add(new DummyCity("Harris",8000,1,"MN",45.5863889,-92.9744444));
//		cities.add(new DummyCity("Dellwood",8000,1,"MN",45.09,-92.9722222));
//		cities.add(new DummyCity("Lake Saint Croix Beach",8000,1,"MN",44.9208333,-92.7666667));
//		cities.add(new DummyCity("Shafer",8000,1,"MN",45.3869444,-92.7475));
//		cities.add(new DummyCity("Taylors Falls",8000,1,"MN",45.4019444,-92.6522222));
//		cities.add(new DummyCity("Martin Lake",8000,1,"MN",45.3811111,-93.0947222));
//		cities.add(new DummyCity("Henderson",8000,1,"MN",44.5283333,-93.9075));
//		cities.add(new DummyCity("Lilydale",6000,1,"MN",44.9161111,-93.1258333));
//		cities.add(new DummyCity("Gibbon",6000,1,"MN",44.5338889,-94.5261111));
//		cities.add(new DummyCity("Landfall",6000,1,"MN",44.9508333,-92.9763889));
//		cities.add(new DummyCity("Hilltop",6000,1,"MN",45.0533333,-93.2472222));
//		cities.add(new DummyCity("Cleveland",6000,1,"MN",44.3255556,-93.8375));
//		cities.add(new DummyCity("Hampton",6000,1,"MN",44.6102778,-93.0019444));
//		cities.add(new DummyCity("Marine on Saint Croix",6000,1,"MN",45.3666667,-92.7708333));
//		cities.add(new DummyCity("Kasota",6000,1,"MN",44.2925,-93.9647222));
//		cities.add(new DummyCity("Loretto",6000,1,"MN",45.0547222,-93.6352778));
//		cities.add(new DummyCity("Elysian",6000,1,"MN",44.1986111,-93.6736111));
//		cities.add(new DummyCity("Clear Lake",6000,1,"MN",45.445,-93.9986111));
//		cities.add(new DummyCity("Center City",6000,1,"MN",45.3938889,-92.8163889));
//		cities.add(new DummyCity("Minnetonka Beach",6000,1,"MN",44.9397222,-93.5763889));
//		cities.add(new DummyCity("Green Isle",6000,1,"MN",44.6791667,-94.0080556));
//		cities.add(new DummyCity("Sunfish Lake",6000,1,"MN",44.8708333,-93.0983333));
//		cities.add(new DummyCity("Foreston",6000,1,"MN",45.7344444,-93.7102778));
//		cities.add(new DummyCity("Hamburg",6000,1,"MN",44.7333333,-93.9669444));
//		cities.add(new DummyCity("Willernie",6000,1,"MN",45.0541667,-92.9563889));
//		cities.add(new DummyCity("Bethel",6000,1,"MN",45.4038889,-93.2675));
//		cities.add(new DummyCity("Big Lake",6000,1,"MN",45.3325,-93.7458333));
//		cities.add(new DummyCity("Woodland",6000,1,"MN",44.9469444,-93.5038889));
//		cities.add(new DummyCity("Randolph",6000,1,"MN",44.5261111,-93.0197222));
//		cities.add(new DummyCity("New Auburn",6000,1,"MN",44.6736111,-94.2294444));
//		cities.add(new DummyCity("Vermillion",6000,1,"MN",44.6736111,-92.9669444));
//		cities.add(new DummyCity("Pine Springs",6000,1,"MN",45.0358333,-92.9541667));
//		cities.add(new DummyCity("Gem Lake",6000,1,"MN",45.0575,-93.0322222));
//		cities.add(new DummyCity("New Germany",6000,1,"MN",44.8841667,-93.9702778));
//		cities.add(new DummyCity("Medicine Lake",6000,1,"MN",44.9952778,-93.4152778));
//		cities.add(new DummyCity("Saint Marys Point",6000,1,"MN",44.9144444,-92.7655556));
//		cities.add(new DummyCity("Lakeland Shores",4000,1,"MN",44.9480556,-92.7638889));
//		cities.add(new DummyCity("Pease",4000,1,"MN",45.6980556,-93.6477778));
//		cities.add(new DummyCity("Wahkon",4000,1,"MN",46.1183333,-93.5208333));
//		cities.add(new DummyCity("Mendota",4000,1,"MN",44.8872222,-93.1641667));
//		cities.add(new DummyCity("South Haven",4000,1,"MN",45.2925,-94.2116667));
//		cities.add(new DummyCity("Kilkenny",4000,1,"MN",44.3133333,-93.5738889));
//		cities.add(new DummyCity("Heidelberg",4000,1,"MN",44.4913889,-93.6261111));
//		cities.add(new DummyCity("Stanchfield",2000,1,"MN",45.6733333,-93.1830556));
//		cities.add(new DummyCity("New Trier",2000,1,"MN",44.6013889,-92.9338889));
//		cities.add(new DummyCity("Bock",2000,1,"MN",45.785,-93.5566667));

		
	}
	
	
 	private void setUserCities(){
		cities = new ArrayList<DummyCity>();
		
		cities.add(new DummyCity("Minneapolis",407207,56,"MN",44.98,-93.2636111));
//		cities.add(new DummyCity("Saint Paul",297640,41,"MN",44.9444444,-93.0930556));
//		cities.add(new DummyCity("Bloomington",86314,12,"MN",44.8408333,-93.2980556));
//		cities.add(new DummyCity("Brooklyn Park",78728,11,"MN",45.0941667,-93.3561111));
//		cities.add(new DummyCity("Plymouth",75057,11,"MN",45.0105556,-93.4552778));
//		cities.add(new DummyCity("Maple Grove",66945,10,"MN",45.0725,-93.4555556));
//		cities.add(new DummyCity("Woodbury",66807,10,"MN",44.9238889,-92.9591667));
//		cities.add(new DummyCity("Eagan",66084,9,"MN",44.8041667,-93.1666667));
//		cities.add(new DummyCity("Eden Prairie",63228,9,"MN",44.8547222,-93.4705556));
//		cities.add(new DummyCity("Coon Rapids",62112,9,"MN",45.12,-93.2875));
//		cities.add(new DummyCity("Burnsville",61630,9,"MN",44.7677778,-93.2775));
//		cities.add(new DummyCity("Blaine",61190,9,"MN",45.1608333,-93.2347222));
//		cities.add(new DummyCity("Lakeville",59866,9,"MN",44.6497222,-93.2425));
//		cities.add(new DummyCity("Minnetonka",51486,7,"MN",44.9133333,-93.5030556));
//		cities.add(new DummyCity("Apple Valley",50487,7,"MN",44.7319444,-93.2175));
//		cities.add(new DummyCity("Edina",49597,7,"MN",44.8897222,-93.3497222));
//		cities.add(new DummyCity("Saint Louis Park",47502,7,"MN",44.9483333,-93.3477778));
//		cities.add(new DummyCity("Maplewood",40199,6,"MN",44.9530556,-92.995));
//		cities.add(new DummyCity("Shakopee",39677,6,"MN",44.7980556,-93.5266667));
//		cities.add(new DummyCity("Richfield",36179,5,"MN",44.8833333,-93.2827778));
//		cities.add(new DummyCity("Cottage Grove",35630,5,"MN",44.8277778,-92.9436111));
//		cities.add(new DummyCity("Roseville",35319,5,"MN",45.0061111,-93.1563889));
//		cities.add(new DummyCity("Inver Grove Heights",34709,5,"MN",44.8480556,-93.0425));
//		cities.add(new DummyCity("Andover",32006,5,"MN",45.2333333,-93.2911111));
//		cities.add(new DummyCity("Brooklyn Center",30729,5,"MN",45.0761111,-93.3325));
//		cities.add(new DummyCity("Savage",29208,4,"MN",44.7791667,-93.3361111));
//		cities.add(new DummyCity("Oakdale",28033,4,"MN",44.9630556,-92.9647222));
//		cities.add(new DummyCity("Fridley",27670,4,"MN",45.0861111,-93.2630556));
//		cities.add(new DummyCity("Shoreview",26194,4,"MN",45.0791667,-93.1469444));
//		cities.add(new DummyCity("Ramsey",25598,4,"MN",45.2461111,-93.4519444));
//		cities.add(new DummyCity("Prior Lake",25039,4,"MN",44.7133333,-93.4225));
//		cities.add(new DummyCity("White Bear Lake",24986,4,"MN",45.0847222,-93.0097222));
//		cities.add(new DummyCity("Chanhassen",24967,4,"MN",44.8622222,-93.5305556));
//		cities.add(new DummyCity("Chaska",24838,4,"MN",44.7894444,-93.6019444));
//		cities.add(new DummyCity("Champlin",23828,4,"MN",45.1888889,-93.3972222));
//		cities.add(new DummyCity("Elk River",23746,4,"MN",45.3038889,-93.5669444));
//		cities.add(new DummyCity("Rosemount",23008,4,"MN",44.7394444,-93.1255556));
//		cities.add(new DummyCity("Crystal",22605,4,"MN",45.0327778,-93.36));
//		cities.add(new DummyCity("Farmington",22571,4,"MN",44.6402778,-93.1433333));
//		cities.add(new DummyCity("Hastings",22566,4,"MN",44.7433333,-92.8522222));
//		cities.add(new DummyCity("New Brighton",22266,4,"MN",45.0655556,-93.2016667));
//		cities.add(new DummyCity("Lino Lakes",20948,3,"MN",45.1602778,-93.0886111));
//		cities.add(new DummyCity("Golden Valley",20866,3,"MN",45.0097222,-93.3488889));
//		cities.add(new DummyCity("New Hope",20792,3,"MN",45.0380556,-93.3863889));
//		cities.add(new DummyCity("South Saint Paul",20487,3,"MN",44.8927778,-93.0347222));
//		cities.add(new DummyCity("West Saint Paul",19802,3,"MN",44.9161111,-93.1013889));
//		cities.add(new DummyCity("Columbia Heights",19775,3,"MN",45.0408333,-93.2627778));
//		cities.add(new DummyCity("Forest Lake",19399,3,"MN",45.2788889,-92.985));
//		cities.add(new DummyCity("Stillwater",18800,3,"MN",45.0563889,-92.8058333));
//		cities.add(new DummyCity("Hopkins",18056,3,"MN",44.925,-93.4625));
//		cities.add(new DummyCity("Anoka",17276,3,"MN",45.1977778,-93.3869444));
//		cities.add(new DummyCity("Saint Michael",17267,3,"MN",45.21,-93.6647222));
//		cities.add(new DummyCity("Buffalo",16312,3,"MN",45.1719444,-93.8744444));
//		cities.add(new DummyCity("Ham Lake",15888,3,"MN",45.2502778,-93.2497222));
//		cities.add(new DummyCity("Otsego",15047,3,"MN",45.2741667,-93.5911111));
//		cities.add(new DummyCity("Robbinsdale",14320,2,"MN",45.0322222,-93.3383333));
//		cities.add(new DummyCity("Hugo",14239,2,"MN",45.16,-92.9930556));
//		cities.add(new DummyCity("Monticello",13236,2,"MN",45.3055556,-93.7938889));
//		cities.add(new DummyCity("Vadnais Heights",13143,2,"MN",45.0575,-93.0736111));
//		cities.add(new DummyCity("Mounds View",12676,2,"MN",45.105,-93.2083333));
//		cities.add(new DummyCity("North Saint Paul",12224,2,"MN",45.0125,-92.9916667));
//		cities.add(new DummyCity("East Bethel",11643,2,"MN",45.3194444,-93.2022222));
//		cities.add(new DummyCity("Rogers",12393,2,"MN",45.1888889,-93.5527778));
//		cities.add(new DummyCity("Waconia",11776,2,"MN",44.8508333,-93.7866667));
//		cities.add(new DummyCity("Mendota Heights",11222,2,"MN",44.8836111,-93.1380556));
//		cities.add(new DummyCity("Big Lake",10660,2,"MN",45.3325,-93.7458333));
//		cities.add(new DummyCity("Little Canada",10329,2,"MN",45.0269444,-93.0875));
//		cities.add(new DummyCity("North Branch",10160,2,"MN",45.5113889,-92.98));
//		cities.add(new DummyCity("Arden Hills",10000,2,"MN",45.0502778,-93.1563889));
//		cities.add(new DummyCity("Mound",10000,2,"MN",44.9366667,-93.6658333));
//		cities.add(new DummyCity("Saint Anthony",10000,2,"MN",45.0205556,-93.2177778));
//		cities.add(new DummyCity("Cambridge",10000,2,"MN",45.5727778,-93.2241667));
//		cities.add(new DummyCity("Oak Grove",10000,2,"MN",45.3408333,-93.3266667));
//		cities.add(new DummyCity("Lake Elmo",10000,2,"MN",44.9958333,-92.8791667));
//		cities.add(new DummyCity("Victoria",10000,2,"MN",44.8586111,-93.6613889));
//		cities.add(new DummyCity("Mahtomedi",10000,2,"MN",45.0697222,-92.9513889));
//		cities.add(new DummyCity("Orono",10000,2,"MN",44.9713889,-93.6041667));
//		cities.add(new DummyCity("Wyoming",10000,2,"MN",45.3363889,-92.9969444));
//		cities.add(new DummyCity("Shorewood",10000,2,"MN",44.9008333,-93.5888889));
//		cities.add(new DummyCity("New Prague",10000,2,"MN",44.5433333,-93.5758333));
//		cities.add(new DummyCity("Saint Francis",10000,2,"MN",45.3869444,-93.3591667));
//		cities.add(new DummyCity("Albertville",10000,2,"MN",45.2377778,-93.6541667));
//		cities.add(new DummyCity("Belle Plaine",10000,2,"MN",44.6227778,-93.7683333));
//		cities.add(new DummyCity("Minnetrista",10000,2,"MN",44.9383333,-93.7175));
//		cities.add(new DummyCity("Spring Lake Park",10000,2,"MN",45.1077778,-93.2377778));
//		cities.add(new DummyCity("Jordan",8000,2,"MN",44.6669444,-93.6266667));
//		cities.add(new DummyCity("Delano",8000,2,"MN",45.0419444,-93.7888889));
//		cities.add(new DummyCity("Corcoran",8000,2,"MN",45.0952778,-93.5472222));
//		cities.add(new DummyCity("Falcon Heights",8000,2,"MN",44.9916667,-93.1661111));
//		cities.add(new DummyCity("Isanti",8000,2,"MN",45.4902778,-93.2475));
//		cities.add(new DummyCity("Saint Paul Park",8000,2,"MN",44.8422222,-92.9911111));
//		cities.add(new DummyCity("Medina",8000,2,"MN",45.0352778,-93.5822222));
//		cities.add(new DummyCity("Circle Pines",8000,2,"MN",45.1486111,-93.1513889));
//		cities.add(new DummyCity("Chisago City",8000,2,"MN",45.3736111,-92.8897222));
//		cities.add(new DummyCity("Dayton",8000,2,"MN",45.2438889,-93.5147222));
//		cities.add(new DummyCity("Oak Park Heights",8000,2,"MN",45.0313889,-92.7927778));
//		cities.add(new DummyCity("Princeton",8000,2,"MN",45.57,-93.5813889));
//		cities.add(new DummyCity("North Oaks",8000,2,"MN",45.1027778,-93.0788889));
//		cities.add(new DummyCity("Becker",8000,2,"MN",45.3933333,-93.8766667));
//		cities.add(new DummyCity("Nowthen",8000,2,"MN",45.3280556,-93.47));
//		cities.add(new DummyCity("Lindstrom",8000,2,"MN",45.3894444,-92.8477778));
//		cities.add(new DummyCity("Rockford",8000,2,"MN",45.0883333,-93.7341667));
//		cities.add(new DummyCity("Watertown",8000,2,"MN",44.9636111,-93.8469444));
//		cities.add(new DummyCity("Wayzata",8000,2,"MN",44.9741667,-93.5063889));
//		cities.add(new DummyCity("Carver",8000,2,"MN",44.7636111,-93.6255556));
//		cities.add(new DummyCity("Grant",8000,2,"MN",45.0844444,-92.9102778));
//		cities.add(new DummyCity("Le Sueur",8000,2,"MN",44.4613889,-93.915));
//		cities.add(new DummyCity("Scandia",8000,2,"MN",45.2536111,-92.8055556));
//		cities.add(new DummyCity("Centerville",8000,2,"MN",45.1630556,-93.0555556));
//		cities.add(new DummyCity("Deephaven",8000,2,"MN",44.9297222,-93.5222222));
//		cities.add(new DummyCity("Bayport",8000,2,"MN",45.0213889,-92.7808333));
//		cities.add(new DummyCity("Independence",8000,2,"MN",45.0252778,-93.7072222));
//		cities.add(new DummyCity("Norwood Young America",8000,2,"MN",44.7736111,-93.9213889));
//		cities.add(new DummyCity("Newport",8000,2,"MN",44.8663889,-93.0002778));
//		cities.add(new DummyCity("Annandale",8000,2,"MN",45.2627778,-94.1241667));
//		cities.add(new DummyCity("Hanover",8000,2,"MN",45.1558333,-93.6661111));
//		cities.add(new DummyCity("Montrose",8000,2,"MN",45.065,-93.9108333));
//		cities.add(new DummyCity("Montgomery",8000,2,"MN",44.4388889,-93.5811111));
//		cities.add(new DummyCity("Afton",8000,2,"MN",44.9027778,-92.7833333));
//		cities.add(new DummyCity("Milaca",8000,2,"MN",45.7558333,-93.6541667));
//		cities.add(new DummyCity("Greenfield",8000,2,"MN",45.1033333,-93.6911111));
//		cities.add(new DummyCity("Cokato",8000,2,"MN",45.0758333,-94.1897222));
//		cities.add(new DummyCity("Le Center",8000,2,"MN",44.3894444,-93.73));
//		cities.add(new DummyCity("Osseo",8000,2,"MN",45.1194444,-93.4022222));
//		cities.add(new DummyCity("Lauderdale",8000,2,"MN",44.9986111,-93.2055556));
//		cities.add(new DummyCity("Saint Bonifacius",8000,2,"MN",44.9055556,-93.7472222));
//		cities.add(new DummyCity("Gaylord",8000,2,"MN",44.5530556,-94.2202778));
//		cities.add(new DummyCity("Excelsior",8000,2,"MN",44.9033333,-93.5661111));
//		cities.add(new DummyCity("Arlington",8000,2,"MN",44.6083333,-94.0802778));
//		cities.add(new DummyCity("Maple Lake",8000,2,"MN",45.2291667,-94.0016667));
//		cities.add(new DummyCity("Lexington",8000,2,"MN",45.1425,-93.1630556));
//		cities.add(new DummyCity("Howard Lake",8000,2,"MN",45.0608333,-94.0730556));
//		cities.add(new DummyCity("Waterville",8000,2,"MN",44.2188889,-93.5677778));
//		cities.add(new DummyCity("Mayer",8000,2,"MN",44.885,-93.8875));
//		cities.add(new DummyCity("Lakeland",8000,2,"MN",44.9563889,-92.7655556));
//		cities.add(new DummyCity("Maple Plain",8000,2,"MN",45.0072222,-93.6555556));
//		cities.add(new DummyCity("Long Lake",8000,2,"MN",44.9866667,-93.5713889));
//		cities.add(new DummyCity("Braham",8000,2,"MN",45.7227778,-93.1705556));
//		cities.add(new DummyCity("Clearwater",8000,2,"MN",45.4194444,-94.0486111));
//		cities.add(new DummyCity("Spring Park",8000,2,"MN",44.9352778,-93.6319444));
//		cities.add(new DummyCity("Cologne",8000,2,"MN",44.7716667,-93.7811111));
//		cities.add(new DummyCity("Tonka Bay",8000,2,"MN",44.9086111,-93.5927778));
//		cities.add(new DummyCity("Stacy",8000,2,"MN",45.3980556,-92.9872222));
//		cities.add(new DummyCity("Winthrop",8000,2,"MN",44.5430556,-94.3661111));
//		cities.add(new DummyCity("Waverly",8000,2,"MN",45.0666667,-93.9661111));
//		cities.add(new DummyCity("Harris",8000,2,"MN",45.5863889,-92.9744444));
//		cities.add(new DummyCity("Dellwood",8000,2,"MN",45.09,-92.9722222));
//		cities.add(new DummyCity("Lake Saint Croix Beach",8000,2,"MN",44.9208333,-92.7666667));
//		cities.add(new DummyCity("Shafer",8000,2,"MN",45.3869444,-92.7475));
//		cities.add(new DummyCity("Taylors Falls",8000,2,"MN",45.4019444,-92.6522222));
//		cities.add(new DummyCity("Martin Lake",8000,2,"MN",45.3811111,-93.0947222));
//		cities.add(new DummyCity("Henderson",8000,2,"MN",44.5283333,-93.9075));
//		cities.add(new DummyCity("Lilydale",6000,1,"MN",44.9161111,-93.1258333));
//		cities.add(new DummyCity("Gibbon",6000,1,"MN",44.5338889,-94.5261111));
//		cities.add(new DummyCity("Landfall",6000,1,"MN",44.9508333,-92.9763889));
//		cities.add(new DummyCity("Hilltop",6000,1,"MN",45.0533333,-93.2472222));
//		cities.add(new DummyCity("Cleveland",6000,1,"MN",44.3255556,-93.8375));
//		cities.add(new DummyCity("Hampton",6000,1,"MN",44.6102778,-93.0019444));
//		cities.add(new DummyCity("Marine on Saint Croix",6000,1,"MN",45.3666667,-92.7708333));
//		cities.add(new DummyCity("Kasota",6000,1,"MN",44.2925,-93.9647222));
//		cities.add(new DummyCity("Loretto",6000,1,"MN",45.0547222,-93.6352778));
//		cities.add(new DummyCity("Elysian",6000,1,"MN",44.1986111,-93.6736111));
//		cities.add(new DummyCity("Clear Lake",6000,1,"MN",45.445,-93.9986111));
//		cities.add(new DummyCity("Center City",6000,1,"MN",45.3938889,-92.8163889));
//		cities.add(new DummyCity("Minnetonka Beach",6000,1,"MN",44.9397222,-93.5763889));
//		cities.add(new DummyCity("Green Isle",6000,1,"MN",44.6791667,-94.0080556));
//		cities.add(new DummyCity("Sunfish Lake",6000,1,"MN",44.8708333,-93.0983333));
//		cities.add(new DummyCity("Foreston",6000,1,"MN",45.7344444,-93.7102778));
//		cities.add(new DummyCity("Hamburg",6000,1,"MN",44.7333333,-93.9669444));
//		cities.add(new DummyCity("Willernie",6000,1,"MN",45.0541667,-92.9563889));
//		cities.add(new DummyCity("Bethel",6000,1,"MN",45.4038889,-93.2675));
//		cities.add(new DummyCity("Big Lake",6000,1,"MN",45.3325,-93.7458333));
//		cities.add(new DummyCity("Woodland",6000,1,"MN",44.9469444,-93.5038889));
//		cities.add(new DummyCity("Randolph",6000,1,"MN",44.5261111,-93.0197222));
//		cities.add(new DummyCity("New Auburn",6000,1,"MN",44.6736111,-94.2294444));
//		cities.add(new DummyCity("Vermillion",6000,1,"MN",44.6736111,-92.9669444));
//		cities.add(new DummyCity("Pine Springs",6000,1,"MN",45.0358333,-92.9541667));
//		cities.add(new DummyCity("Gem Lake",6000,1,"MN",45.0575,-93.0322222));
//		cities.add(new DummyCity("New Germany",6000,1,"MN",44.8841667,-93.9702778));
//		cities.add(new DummyCity("Medicine Lake",6000,1,"MN",44.9952778,-93.4152778));
//		cities.add(new DummyCity("Saint Marys Point",6000,1,"MN",44.9144444,-92.7655556));
//		cities.add(new DummyCity("Lakeland Shores",4000,1,"MN",44.9480556,-92.7638889));
//		cities.add(new DummyCity("Pease",4000,1,"MN",45.6980556,-93.6477778));
//		cities.add(new DummyCity("Wahkon",4000,1,"MN",46.1183333,-93.5208333));
//		cities.add(new DummyCity("Mendota",4000,1,"MN",44.8872222,-93.1641667));
//		cities.add(new DummyCity("South Haven",4000,1,"MN",45.2925,-94.2116667));
//		cities.add(new DummyCity("Kilkenny",4000,1,"MN",44.3133333,-93.5738889));
//		cities.add(new DummyCity("Heidelberg",4000,1,"MN",44.4913889,-93.6261111));
//		cities.add(new DummyCity("Stanchfield",2000,1,"MN",45.6733333,-93.1830556));
//		cities.add(new DummyCity("New Trier",2000,1,"MN",44.6013889,-92.9338889));
//		cities.add(new DummyCity("Bock",2000,1,"MN",45.785,-93.5566667));



	}

}



