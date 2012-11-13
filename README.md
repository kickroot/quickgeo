## QuickGeo
### An awesome in-memory global postal code database for the dangerously pragmatic Java developer

**What does that even mean?**  
Rather than give you a dry feature list, let's quickly examine the types of questions that QuickGeo can answer for you:

1. How many miles (or kilometers) is New York City from London?
2. Show me all cities/towns/villages within 25 miles (or kilometers) of Reykjavik, Iceland
3. Show me all cities/towns/villages that match the regex ".*toronoto.*"
4. What cities/towns/villages have the postal code "18431"?
5. Give me a bounding box that covers 29 miles (or kilometers) in each direction around New Delhi, India

**Why in-memory and not a web service**
Because it's awesome.  
Here's why:

1. No need to have an Internet connection
2. No third party service to yank your API access if your app suddenly goes viral
3. Your Android phone can easiliy handle the memory requirements for multiple countries (you get to choose which you include in your app)
4. Lookup is *fast*. Hello Twitter Bootstrap TypeAhead!
5. Lookups are an inexpensive operation and can be done as the user types (see item 4).

