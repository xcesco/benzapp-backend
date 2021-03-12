/**
 * 
 * fix per menu mobile  
 * 
 */
$(function() {
    $(window).resize(function() {
      // reset parametri click iniettati su mobile
      if (window.matchMedia('(min-width: 993px)').matches) {
        $('.navbar .dropdown-menu').removeAttr('style')
      }
    })
})