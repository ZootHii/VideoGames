# VideoGames
 video games -> list, search, add favorites android task
 
 - MVVM design<br>
 - Dependency Inecjtion (Dagger Hilt)<br>
 - Retrofit2 (API)<br>
 - Room (Database)<br>
 - Paging RecyclerView (PagingSource)<br>
 - Firebase Analytics/Crashlytics<br>
 - Lifecycle/Coroutine<br>
 - Viewpager2/CircleIndicator3<br>
 - NavigationDirections<br>
 - Collapsing Toolbar<br>
 - Coil (ImageView URL)<br>
 
 Favorites has search from database<br>
 Games/Home has search from getGames api @Query("search")<br>
 Search not found and empty favorites messages<br>
 Parcelable model logs Firebase Analytics in ViewModel for add-remove game favorites and get game details<br>
 PagingDataAdapter for Games/Home recyclerview<br>
 Add-Remove Favorites buton icon change and remembers isFavorite<br>
 Add-Remove Favorites snacbar with item name<br>
 Collapsing image for game details page<br>
 

 I can save games in room database but I used only for favorites, for games/home games I used api's search query
 
