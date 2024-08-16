var sidebar_open;
var sidebar_width;


function load(){
    sidebar_open = false;
    sidebar_width = '250px';

    $('#title').text($('.active').text());
    $('.active').prop('disabled', true);
    document.title = $('.active').text();

    setSidebarState();
}

$( window ).resize(function() {
    setSidebarState();
});

/*TOGGLER*/
function setSidebarState(){
    /*xs*/
    if (window.matchMedia("(max-width: 576px)").matches) {
        sidebar_width='250px';
        if(sidebar_open)
            toggle();
        $('#user-panel-navbar').hide();
        $('#user-panel-sidebar').show();
    }
    /*sm*/
    if (window.matchMedia("(min-width: 576px) and (max-width: 768px)").matches) {
        sidebar_width='250px';
        if(sidebar_open)
            toggle();
        $('#user-panel-navbar').show();
        $('#user-panel-sidebar').hide();
    }
    /*md*/
    if (window.matchMedia("(min-width: 768px) and (max-width: 992px)").matches) {
        sidebar_width='300px';
        if(!sidebar_open)
            toggle();
        else{
            openNav();
            sidebar_open = true;
        }
        $('#user-panel-navbar').show();
        $('#user-panel-sidebar').hide();
    }
    /*lg*/
    if (window.matchMedia("(min-width: 992px)").matches) {
        sidebar_width='350px';
        if(!sidebar_open)
            toggle();
        else{
            openNav();
            sidebar_open = true;
        }
        $('#user-panel-navbar').show();
        $('#user-panel-sidebar').hide();
    }
}

function toggle(){
    if(sidebar_open){
        closeNav();
        sidebar_open = false;
    }
    else{
        openNav();
        sidebar_open = true;
    }
}

function openNav() {
    document.getElementById("mySidebar").style.width = sidebar_width;
    document.getElementById("main").style.marginLeft = sidebar_width;
    document.getElementById("navbar").style.boxShadow = sidebar_width+" 2px 5px #000";
}

function closeNav() {
    document.getElementById("mySidebar").style.width = "0";
    document.getElementById("main").style.marginLeft= "0";
    document.getElementById("navbar").style.boxShadow = "0px 2px 5px #000";
}


/*USER PANEL*/
$('#change-password').click(function (e){
    $('#modal_change-password').modal('show');
});

$('#settings').click(function (e){
    $('#modal_settings').modal('show');
});

