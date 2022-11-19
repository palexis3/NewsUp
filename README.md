# NewsUp
I first wrote the NewsUp! App in 2017 to learn Android and haven’t touched it since then. After looking through the code, I realized that
it’s definitely time (it really reeks of some bad architectural patterns 🙂) to update to a modern stack using the new shiny tools that 
the Android world has to offer. As such, I took some time to weigh my options to make this migration meaningful and a great learning experience. 
Of course, I’d be using Jetpack Compose, Dagger Hilt, Moshi and Flows as the pillars of the new app.

I was also intrigued in playing around with [Airbnb's Maverick](https://airbnb.io/mavericks/) framework due to the premise it's built on top of the
Jetpack library and Kotlin coroutines. In other words, the framwork helps make complex problems that typically arise in most apps such as threading,
state synchronization, etc easier to deal with. Since I'm using Jetpack compose, the framework would primarily be used for business logic and data 
fetching while composables consumes the state emitted from [Mavericks viewmodel](https://airbnb.io/mavericks/#/core-concepts?id=mavericksviewmodel)
(i.e *MavericksViewModel*)

Here's a [google doc](https://docs.google.com/document/d/17kl2rR64WoK-xAIIExG7tvQdH6NfzdK0yFRfj-cxigM/edit?usp=sharing) that notes some of the
interesting issues I encountered and some of the design decisions that were made.


## UI

 ### Headlines Screen
  
  The Headlines screen makes a fetch to the `../top-headlines` [endpoint](https://newsapi.org/docs/endpoints/top-headlines) from NewsApi.org and with
  this endpoint there is a set of categories that can be appended to the query to receive a liist of news articles based on the associated category.
  Since there isn't a viewpager implementation for Jetpack compose, I instead opted for the [Accompanist horizontal pager](https://google.github.io/accompanist/pager/) 
  that mimics a viewpager.

  https://user-images.githubusercontent.com/16326086/202833281-daa5ec3a-5e84-4261-8b76-5fe5d3f559a7.mp4

 ### News Sources Screen
 
  The News Sources screen pings the `../sources` [endpoint](https://newsdata.io/docs) from NewsData.io since it contained more news sources than 
  NewsApi.org and seemed more inclusive of different countries/languages. A user can select a single category to make the search more refined. Notice that   the category items are composed of single selection chips that wrap to the next line using [FlowRow](https://google.github.io/accompanist/flowlayout/). .   To pair with the category chips is a LazyColumn that shows cards of each news source with its name. Check out the entire [PR]  (https://github.com/palexis3/NewsUp/pull/9)
 
  https://user-images.githubusercontent.com/16326086/202833309-8b46f415-1947-4dc0-8775-e8252bcf7b42.mp4


 ### Search Screen
 
  The Search screen makes a fetch to the `../get-everything` [endpoint](https://newsapi.org/docs/endpoints/everything) from NewsApi.org and 
  makes use of Maverick's [Async](https://airbnb.io/mavericks/#/async) sealed class to handle the `Unitialized -> Loading -> Success/Failure` states 
  without any fancy operators to be done on the stateflow being collected in the Search screen. In the Sarch screen composable, all that has to be done
  is a mapping of what to do for the different states in typical MVI scenarios. Here's the [PR](https://github.com/palexis3/NewsUp/pull/15)

  https://user-images.githubusercontent.com/16326086/202833357-0abc2d05-f090-45fd-96fa-5f24dc1cc9b2.mp4


 ### Webview Screen using Chrome Tabs
 
  Previously implemented using [WebView](https://developer.android.com/develop/ui/views/layout/webapps/webview) to show news articles and sources 
  but noticed how it wasn't smooth and would regularly not load correctly. As such, I migrated web content logic to using Chrome Tabs. Here's the [PR](https://github.com/palexis3/NewsUp/pull/17)
 
  https://user-images.githubusercontent.com/16326086/202833392-9873477b-2804-4d4c-9288-7f3f3f487cfc.mp4
  
  
 ### Preferences Screen
 
  For every endpoint that we could hit, there's a `language` and/or `country` query parameter that can be assigned to meet the user's preference. As such,
  it makes sense for the user to set these values in a single place and be used in queries afterwards to meet a user's language preference or country they're
  intrigued in. Jetpack's [Datastore](https://developer.android.com/topic/libraries/architecture/datastore) library is the recommended preferences manager
  to use in such cases. Check out the [PR](https://github.com/palexis3/NewsUp/pull/16)
 
  https://user-images.githubusercontent.com/16326086/202833471-0f0f6bb9-c0a1-44cf-90e1-beaf20452bdc.mp4
  
  
## Technologies

- Airbnb Maverick Framework
- Jetpack Compose
- Datastore
- Retrofit
- Material Design 3
- Moshi
- Dagger Hilt








