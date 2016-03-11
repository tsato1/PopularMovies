# *PopularMovies*

**PopularMovies** is an Android application that showcases movies according three different sorting criteria - Most Popular, High Rated, and Favorites. Detail page is provided for each movie where you can share it on other contents like email, watch trailers, and see other people's review.

Udacity Course Project : Submitted by: **Takahide Sato**

## User Stories

The following **required** functionality is completed:

* [ ] Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.
* [ ] UI contains an element (i.e a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.
* [ ] UI contains a screen for displaying the details for a selected movie.
* [ ] Movie details layout contains title, release date, movie poster, vote average, and plot synopsis.
* [ ] When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.
* [ ] When a movie poster thumbnail is selected, the movie details screen is launched [Phone] or displayed in a fragment [Tablet].
* [ ] In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.
* [ ] Movie Details layout contains a section for displaying trailer videos and user reviews.
* [ ] Tablet UI uses a Master-Detail layout implemented using fragments. The left fragment is for discovering movies. The right fragment displays the movie details view for the currently selected movie.
* [ ] When a trailer is selected, app uses an Intent to launch the trailer.
* [ ] In the movies detail screen, a user can tap a star button to mark it as a Favorite.
* [ ] App saves a “Favorited” movie to SharedPreferences or a database using the movie’s id.
* [ ] When the “favorites” setting option is selected, the main view displays the entire favorites collection based on movie IDs stored in SharedPreferences or a database.

The following **optional** features are implemented:

* [ ] App persists favorite movie details using a database.
* [ ] App displays favorite movie details (title, poster, synopsis, user rating, release date) even when offline.
* [ ] App uses a ContentProvider to populate favorite movie details.
* [ ] Movie Details View includes an Action Bar item that allows the user to share the first trailer video URL from the list of trailers.
* [ ] App uses a share Intent to expose the external youtube URL for the trailer.

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/WqFtfAI.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## License

    Copyright [2016] [Takahide Sato]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
