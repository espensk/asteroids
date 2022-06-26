# Asteroids scanner
## Task
 1. Show 10 asteroids that passed the closest to Earth between two user-provided dates.
 2. Show characteristics of the largest asteroid (estimated diameter) passing Earth during a user-provided year.
 
Using Java, implement a RESTful API service to answer such queries. The service should employ some kind of caching to avoid extra external API calls.
> Now, task 1 seems a simple caching API service, as NASA's Asteroids API already provide a search of asteroids between two dates.
> Task 2 asks us to show the diameter of the largest body within a year.

> Note: The cache requirement seems overkill, though it can be needed in similar applications.
  The complexity in this task relies in the cache storage; we'd like to reuse the cache storage across queries, possibly adjacent or overlapping. How can we retrieve range data from earlier partially overlapping queries?

## API
Defining two APIs for the two tasks:

GET asteroids/close
params: start_date
        end_date limit (default 10)

Return a json list of asteroids (simplifying the datastructure from NASA)
   id
   name
   diameter (use max, in meters)
   distance (min, in kilometers)
   link

GET asteroids/biggest
params: year
end_date
limit (default 10)

We can return an Asteroid object same as above.

## Cache database design
NASA APIs' query limit is spanning 7 days. So our task to calculate the biggest asteroid passing in a year needs more than 50 repeated queries.
Caching will definitely speed this up if we do repeated queries over the same intervals.
As we receive json data from nasa, with slightly complex structures of lists of objects, I'd recommend a json or text store over a SQL database. 
MongoDB is a clear candidate if parameters are to be indexed/searched, but also memcached/redis can serve our purpose (text key/value stores, the latter with persistence).

NASA returns asteroid data to us as a map of date -> list of asteroids.
As maximum searches are 7 days, I've designed the queriying to be one per day. It is not a huge performance loss (1/7) compared to querying a week at a time,
and this avoids complex evaluation of cache presence over partially overlapping queries.
(we will have to check the cache for every day within the query regardless, as that's the smallest unit of query)
Downside of this is that the query for the biggest asteroid in a year takes > 15 minutes. 
Observing roughlt 3 seconds per query and needing 356 of them makes for a very inefficient check. 
The second is very quick though:) 

With the requested features, persistence is not very important, as the truth set comes directly from nasa itself and is not modified by our app.
If the database is restared frequently this could change though, but that's not often seen.
For other applications or features, we'd possibly need to modify or amend the data itself, where persistence would become important.

## Implementation
Decided to use grizzly/jersey as web framework as it's widely used and not overly bloated.
Logging using slf4j to stdout.

## Use docker?
Docker is great, but it doesn't really help other developers to get started. 
It does rather add another level of complexity to get to run it.
Docker helps a lot to deploy and run the software though, as an image can be published in a registry and easily installed and run on a docker runtime on a lot of computers.
Even simpler using kubernetes and a helm chart pointing to the image.

## Further improvements
- A lot:)
- Add a real database, I'd go with memcached as it's simple. Until a real persistence requirement come.
- Fork queries to NASA in parallel, using client's rx() api and CompletableFutures.  
  It causes some more complexity so I thought I'd test it with synchronous requests first, but never got time to paralellize it.
- Possibly add a more intelligent cache presence check so that we can query 7 days at NASA, cutting # requests by 1/7.
- Add Dockerfile to bundle this as an image and publish it
- Add a k8s cluster with database (memcached?) 
- For now we're logging to stdout as it's simple. However as we use the logging facade slf4j it's simple to wire in more comprehensive logging later. 
- Dates. It's weird that the NASA APIs present dates without timezone? 
  I have for now ignored timezone in the app by using localtime, but in real world this must be accounted for.