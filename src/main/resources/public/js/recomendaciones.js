/*SCROLL*/

var isIntop = true;

$('#scroll-btn').click(function (e){
    if(isIntop){
        scrollToBotom();
        isIntop = false;
    }
    else{
        scrollToTop();
        isIntop = true;
    }
});

function scrollToBotom(){
    $("html, body").animate({
        scrollTop: $('html, body').get(0).scrollHeight
    }, 800);

    $('#arrow').removeClass('face-down');
    $('#arrow').addClass('face-up');
}

function scrollToTop(){
    $('html, body').animate({
        scrollTop : 0
    }, 800);
    
    $('#arrow').removeClass('face-up');
    $('#arrow').addClass('face-down');
}

/*RECOMENDACION*/
$('.recomendacion').click(function(e){
    $('#staticBackdrop').modal('show');
});