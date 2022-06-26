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
        end_date
        limit (default 10)
Return a json list of asteroids
   id
   name
   diameter (use max, in meters)
   link

GET asteroids/biggest
params: year
end_date
limit (default 10)

Return a json object
  id
  name
  diameter (use max, in meters)
  link


## Cache database design
As we receive json data from nasa, with slightly complex structures of lists of objects, I'd recommend a json or text store over a SQL database. 
With the requested features, persistence is not very important, as the truth set comes directly from nasa itself and is not modified by our app.
(In fact, caching isn't very important here at all, and introduce more complexity than benefit imho:) 
For other applications, we'd possibly modify or amend the data itself, where persistence would become important.


## Implementation
Decided to use grizzly/jersey as web framework as it's widely used and not overly bloated.
Logging using slf4j to stdout.

## Use docker?
Docker is great, but it doesn't really help other developers to get started. 
It does rather add another level of complexity to get to run it.
Docker helps a lot to deploy and run the software though, as an image can be published in a registry and easily installed and run on a docker runtime on a lot of computers.
Even simpler using kubernetes and a helm chart pointing to the image.

## Further improvements
- A lot. 
- Logging to stdout now as it's simple. However as we use the logging facade slf4j it's simple to wire in more comprehensive logging later.
- Dates. It's weird that the NASA APIs present dates without timezone. It should be. 
  I have for now ignored timezone in the app, but in real world this must be accounted for.