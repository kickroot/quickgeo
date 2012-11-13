## QuickGeo
### An awesome in-memory global postal code database for the dangerously pragmatic Java developer

**What does that even mean?**  
Rather than give you a dry feature list, let's quickly examine the types of questions that QuickGeo can answer for you:

1. How many miles (or kilometers) is New York City from London?
2. Show me all cities/towns/villages within 25 miles (or kilometers) of Reykjavik, Iceland
3. Show me all cities/towns/villages that match the regex ".*toronoto.*"
4. What cities/towns/villages have the postal code "18431"?
5. Give me a bounding box that covers 29 miles (or kilometers) in each direction around New Delhi, India

**Why in-memory and not a web service?**  
Because it's awesome. Here's why:

1. No need to have an Internet connection
2. No third party service to yank your API access if your app suddenly goes viral
3. Your Android phone can easily handle the memory requirements for multiple countries (you get to choose which you include in your app)
4. Lookup is *fast*. Hello Twitter Bootstrap TypeAhead!
5. Lookups are an inexpensive operation and can be done as the user types (see item 4).

## Other cool things about QuickGeo

### It's modular
You choose exactly which countries you want to include.  Want Estonia, Iceland, and Australia?  No problem.

### It uses Maven
Managing all those country specific jar files is not your problem

### It's Open Source
[Hello Mozilla Public License v2.0](http://www.mozilla.org/MPL/)


## How do I use this in my Maven based app?
I'm glad you asked!  You'll need to add some dependencies to your project as such:

```xml
<dependency>
  <groupId>org.quickgeo</groupId>
  <artifactId>quickgeo-core</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
<dependency>
  <groupId>org.quickgeo</groupId>
  <artifactId>quickgeo-us</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
```

 In the above sample, you add the required *quickgeo-core* artifact (which contains the public facing interface), and the postal code database for the US. You get one country per added dependency.  So if you wanted to include Canada you would add another dependeny:

```xml
<dependency>
  <groupId>org.quickgeo</groupId>
  <artifactId>quickgeo-ca</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>

There are quite a few countries to choose from, and each one gets it's own Maven project within QuickGeo.

***At the moment this project is not hosted in Maven Central, I'm working on that.***  
This means to use QuickGeo you'll need to clone the repo and build it yourself.  Seriously it takes like 2 minutes.


##Where did you get the postal code data from?  
From the awesome people at [GeoNames](http://www.geonames.org/)
The work is covered under the Creative Commons Attribution 3.0 License. This means you need to give them credit if you use QuickGeo in your application or service.  Sharing is caring, eh?

