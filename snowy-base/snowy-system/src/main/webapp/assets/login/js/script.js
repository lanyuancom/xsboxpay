(function($) {

    $(document).ready(function () {
        // If svg is not supported
        if (!Modernizr.svg) {
            $('img[src$=".svg"]').each(function() {
                $(this).attr('src', $(this).attr('src').replace('.svg', '.png'));
            });
        }
        // If media queries are not supported
        if(!Modernizr.mq('only all')) {
            $('head').append('<link id="no-mq" rel="stylesheet" type="text/css">');
            $("link#no-mq").attr("href", "/joomla/media/templates/highsoft_bootstrap/css/ie.css");
        }
        // Sidebar click animation
        $('.nav-sidebar > li').click(function () {
            if (!$(this).hasClass("active")) {
                $('.nav-sidebar > li.active > div.active').removeClass('active');
                $('.nav-sidebar > li.active > ul').slideUp("slow");
                $('.nav-sidebar > li.active').removeClass('active');
                $(this).addClass("active");
                $('.nav-sidebar > li.active > ul').slideDown("slow");
                $('.nav-sidebar > li.active > div').addClass('active');
            }
        });
        $("#sidebar-toggle").click(function (e) {
            $("#wrap").toggleClass("toggled");
        });
    });
})(jQuery);
