/*
   This is a very simple example of a web front end for a publicly available web service.
   Due to its pedagogical nature, comments are more elaborate than they typically need to
   be, or may even be present when no developer explanation would usually be necessary.

   Further, this example uses JavaScript ES6 syntax.
   */
   "use strict";

// Yes, this is a "global." But it is a single entry point for all of the code in the module,
// and in its role as the overall controller code of the page, this is one of the acceptable
// uses for a [single!] top-level name.
//
// Module managers address even this issue, for web apps of sufficient complexity.
   window.ImgurSearchController = (() => {
       return {
           init: () => {
               var searchButton = $("#search-button");
               var searchTerm = $("#search-term");
               var imageResultContainer = $('.image-result-container');
               searchButton.click(() => {
                   // The getJSON function initiates a connection to the web service.
                   $.ajax({
                       dataType: "json",
                       url: "https://api.imgur.com/3/gallery/search/",
                       data: {q: searchTerm.val()},
                       headers: {Authorization: "Client-ID a0f70f5015d9134"}
                   }).done((result) => {
                       let albums = result.data.filter((image) => {
                           return image.is_album && !image.nsfw;
                       });
                       let images = result.data.filter((image) => {
                           return !image.is_album && !image.nsfw;
                       });

                
                       imageResultContainer.empty();

                       albums.forEach((currentAlbum) => {
                           $.ajax({
                               dataType: "json",
                               url: "https://api.imgur.com/3/album/" + currentAlbum.id + "/images",
                               headers: {Authorization: "Client-ID a0f70f5015d9134"}
                           }).done((result2) => {
                               // Receiving the response renders it in an HTML element tree then
                               // appends it to the element(s) with the class image-result-container.
                               imageResultContainer.append(
                                   result2.data.map((image) => {
                                       return $("<div></div>").addClass("col-xs-6 col-sm-4 col-md-3 col-lg-2").append(
                                           $("<div></div>").addClass("thumbnail").append(
                                               $("<img/>").attr({
                                                   src: image.link,
                                                   alt: image.title,
                                               })
                                           )
                                           );
                                   })
                               );
                           });
                       });

                       images.forEach((currentImage) => {
                           $.ajax({
                               dataType: "json",
                               url: "https://api.imgur.com/3/image/" + currentImage.id,
                               headers: {Authorization: "Client-ID a0f70f5015d9134"}
                           }).done((result3) => {
                               // Receiving the response renders it in an HTML element tree then
                               // appends it to the element(s) with the class image-result-container.
                               imageResultContainer.append(
                                   $("<div></div>").addClass("col-xs-6 col-sm-4 col-md-3 col-lg-2").append(
                                       $("<div></div>").addClass("thumbnail").append(
                                           $("<img/>").attr({
                                               src: result3.data.link,
                                               alt: result3.data.title
                                           })
                                       )
                                   )
                               );
                           });
                       });
                   });
               });

               var galleryButton = $("#gallery-button");
               galleryButton.click(() => {
                   $.ajax({
                       dataType: "json",
                       url: "https://api.imgur.com/3/gallery/hot/viral/0.json",
                       headers: {Authorization: "Client-ID 4211f725b72b537"}
                   }).done((result) => {
                       let albums = result.data.filter((image) => {
                           return image.is_album && !image.nsfw;
                       });
                       let images = result.data.filter((image) => {
                           return !image.is_album && !image.nsfw;
                       });

                
                       imageResultContainer.empty();

                       albums.forEach((currentAlbum) => {
                           $.ajax({
                               dataType: "json",
                               url: "https://api.imgur.com/3/album/" + currentAlbum.id + "/images",
                               headers: {Authorization: "Client-ID a0f70f5015d9134"}
                           }).done((result2) => {
                               // Receiving the response renders it in an HTML element tree then
                               // appends it to the element(s) with the class image-result-container.
                               imageResultContainer.append(
                                   result2.data.map((image) => {
                                       return $("<div></div>").addClass("col-xs-6 col-sm-4 col-md-3 col-lg-2").append(
                                           $("<div></div>").addClass("thumbnail").append(
                                               $("<img/>").attr({
                                                   src: image.link,
                                                   alt: image.title,
                                               })
                                           )
                                       );
                                   })
                               );
                           });
                       });

                       images.forEach((currentImage) => {
                           $.ajax({
                               dataType: "json",
                               url: "https://api.imgur.com/3/image/" + currentImage.id,
                               headers: {Authorization: "Client-ID a0f70f5015d9134"}
                           }).done((result3) => {
                               imageResultContainer.append(
                                   $("<div></div>").addClass("col-xs-6 col-sm-4 col-md-3 col-lg-2").append(
                                       $("<div></div>").addClass("thumbnail").append(
                                           $("<img/>").attr({
                                               src: result3.data.link,
                                               alt: result3.data.title
                                           })
                                       )
                                   )
                               );
                           });
                       });
                   });  
               }); 

               var memesButton = $("#meme-button");

               memesButton.click(() => {
                   $.ajax({
                       url: "https://api.imgur.com/3/memegen/defaults",
                       headers: {Authorization: "Client-ID 4211f725b72b537"}
                   }).done((result) => {
                       let albums = result.data.filter((image) => {
                           return image.is_album && !image.nsfw;
                       });
                       let images = result.data.filter((image) => {
                           return !image.is_album && !image.nsfw;
                       });

                
                       imageResultContainer.empty();
                    // albumsContainer.empty().append(

                       albums.forEach((currentAlbum) => {
                           $.ajax({
                               dataType: "json",
                               url: "https://api.imgur.com/3/album/" + currentAlbum.id + "/images",
                               headers: {Authorization: "Client-ID a0f70f5015d9134"}
                           }).done((result2) => {
                               result2.data.map((image) => {
                                   return $("<div></div>").addClass("col-xs-6 col-sm-4 col-md-3 col-lg-2").append(
                                       $("<div></div>").addClass("thumbnail").append(
                                           $("<img/>").attr({
                                               src: image.link,
                                               alt: image.title,
                                           })
                                       )
                                   );
                               });
                           });
                       });

                       images.forEach((currentImage) => {
                           $.ajax({
                               dataType: "json",
                               url: "https://api.imgur.com/3/image/" + currentImage.id,
                               headers: {Authorization: "Client-ID a0f70f5015d9134"}
                           }).done((result3) => {
                               imageResultContainer.append(
                                   $("<div></div>").addClass("col-xs-6 col-sm-4 col-md-3 col-lg-2").append(
                                       $("<div></div>").addClass("thumbnail").append(
                                           $("<img/>").attr({
                                               src: result3.data.link,
                                               alt: result3.data.title
                                           })
                                       )
                                   )
                               );
                           });
                       });

                   });
               });

               var topicButton1 = $("#topic-button1");
               topicButton1.click(() => {
                   $.ajax({
                       url: "https://api.imgur.com/3/topics/Halloween",
                       headers: {Authorization: "Client-ID 4211f725b72b537"}
                   }).done((result) => {
                       let albums = result.data.filter((image) => {
                           return image.is_album && !image.nsfw;
                       });
                       let images = result.data.filter((image) => {
                           return !image.is_album && !image.nsfw;
                       });

                
                       imageResultContainer.empty();
                       albums.forEach((currentAlbum) => {
                           $.ajax({
                               dataType: "json",
                               url: "https://api.imgur.com/3/album/" + currentAlbum.id + "/images",
                               headers: {Authorization: "Client-ID a0f70f5015d9134"}
                           }).done((result2) => {
                               // Receiving the response renders it in an HTML element tree then
                               // appends it to the element(s) with the class image-result-container.
                               imageResultContainer.append(
                                   result2.data.map((image) => {
                                       return $("<div></div>").addClass("col-xs-6 col-sm-4 col-md-3 col-lg-2").append(
                                           $("<div></div>").addClass("thumbnail").append(
                                               $("<img/>").attr({
                                                   src: image.link,
                                                   alt: image.title,
                                               })
                                           )
                                       );
                                   })
                               );
                           });
                       });

                       images.forEach((currentImage) => {
                           $.ajax({
                               dataType: "json",
                               url: "https://api.imgur.com/3/image/" + currentImage.id,
                               headers: {Authorization: "Client-ID a0f70f5015d9134"}
                           }).done((result3) => {
                               imageResultContainer.append(
                                   $("<div></div>").addClass("col-xs-6 col-sm-4 col-md-3 col-lg-2").append(
                                       $("<div></div>").addClass("thumbnail").append(
                                           $("<img/>").attr({
                                               src: result3.data.link,
                                               alt: result3.data.title
                                           })
                                       )
                                   )
                               );
                           });
                       });
                   });  
               });

               var topicButton2 = $("#topic-button2");
               topicButton2.click(() => {
                   $.ajax({
                       url: "https://api.imgur.com/3/topics/Funny",
                       headers: {Authorization: "Client-ID 4211f725b72b537"}
                   }).done((result) => {
                       let albums = result.data.filter((image) => {
                           return image.is_album && !image.nsfw;
                       });
                       let images = result.data.filter((image) => {
                           return !image.is_album && !image.nsfw;
                       });

                       imageResultContainer.empty();
                       albums.forEach((currentAlbum) => {
                           $.ajax({
                               dataType: "json",
                               url: "https://api.imgur.com/3/album/" + currentAlbum.id + "/images",
                               headers: {Authorization: "Client-ID a0f70f5015d9134"}
                           }).done((result2) => {
                               imageResultContainer.append(
                                   result2.data.map((image) => {
                                       return $("<div></div>").addClass("col-xs-6 col-sm-4 col-md-3 col-lg-2").append(
                                           $("<div></div>").addClass("thumbnail").append(
                                               $("<img/>").attr({
                                                   src: image.link,
                                                   alt: image.title,
                                               })
                                           )
                                       );
                                   })
                               );
                           });
                       });

                       images.forEach((currentImage) => {
                           $.ajax({
                               dataType: "json",
                               url: "https://api.imgur.com/3/image/" + currentImage.id,
                               headers: {Authorization: "Client-ID a0f70f5015d9134"}
                           }).done((result3) => {
                               imageResultContainer.append(
                                   $("<div></div>").addClass("col-xs-6 col-sm-4 col-md-3 col-lg-2").append(
                                       $("<div></div>").addClass("thumbnail").append(
                                           $("<img/>").attr({
                                               src: result3.data.link,
                                               alt: result3.data.title
                                           })
                                       )
                                   )
                               );
                           });
                       });
                   });  
               });

               var topicButton3 = $("#topic-button3");
               topicButton3.click(() => {
                   $.ajax({
                       url: "https://api.imgur.com/3/topics/Science_and_Tech",
                       headers: {Authorization: "Client-ID 4211f725b72b537"}
                   }).done((result) => {
                       let albums = result.data.filter((image) => {
                           return image.is_album && !image.nsfw;
                       });
                       let images = result.data.filter((image) => {
                           return !image.is_album && !image.nsfw;
                       });
    
                       imageResultContainer.empty();
                       albums.forEach((currentAlbum) => {
                           $.ajax({
                               dataType: "json",
                               url: "https://api.imgur.com/3/album/" + currentAlbum.id + "/images",
                               headers: {Authorization: "Client-ID a0f70f5015d9134"}
                           }).done((result2) => {
                               // Receiving the response renders it in an HTML element tree then
                               // appends it to the element(s) with the class image-result-container.
                               imageResultContainer.append(
                                   result2.data.map((image) => {
                                       return $("<div></div>").addClass("col-xs-6 col-sm-4 col-md-3 col-lg-2").append(
                                          $("<div></div>").addClass("thumbnail").append(
                                             $("<img/>").attr({
                                                 src: image.link,
                                                 alt: image.title,
                                             })
                                       )
                                    );
                                   })
                              );
                           });
                       });

                       images.forEach((currentImage) => {
                           $.ajax({
                               dataType: "json",
                               url: "https://api.imgur.com/3/image/" + currentImage.id,
                               headers: {Authorization: "Client-ID a0f70f5015d9134"}
                           }).done((result3) => {
                               // Receiving the response renders it in an HTML element tree then
                               // appends it to the element(s) with the class image-result-container.
                               imageResultContainer.append(
                                   $("<div></div>").addClass("col-xs-6 col-sm-4 col-md-3 col-lg-2").append(
                                       $("<div></div>").addClass("thumbnail").append(
                                           $("<img/>").attr({
                                               src: result3.data.link,
                                               alt: result3.data.title
                                           })
                                       )
                                 )
                              );
                           });
                       });
                   });  
               });

               var uploadButton = $("#upload-button");
               var uploadLink = $("#upload-link");

               uploadButton.click(() => {
                   $.ajax({ 
                       url: 'https://api.imgur.com/3/image',
                       headers: {
                           Authorization: 'Client-ID 05e3165454ec14b'
                       },
                       data: {
                           'image': uploadLink.val()
                       },
                       method: 'POST',
                       //type: 'POST',
                   }).done((result) => {
                       window.location.href = result.data.link;
                   });
               });


           }
       };
   })();
