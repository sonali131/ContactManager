console.log("This is script file")

const toggleSidebar=()=>{
    if($('.sidebar').is(":visible")){
        //true
        //than close it
        $(".sidebar").css("display","none");
        $(".content").css("margin-left","0%");


    }else{
        //false
        //than show it
        $(".sidebar").css("display","block");
        $(".content").css("margin-left","20%");
    }
};

const search=()=>{
    //console.log("Called searching...");

    let query=$("#search-input").val();
    console.log(query);

    if(query==''){
        $(".search-result").hide();

    }
    else{
        //search
        console.log(query);

//sending request to server

let url=`http://localhost:1991/search/${query}`;

fetch(url).then((response)=>{
    return response.json();

}).then(data=>{
    //data....
    //console.log(data);

    // let text=`<div class='list-group'>`
    // data.forEach((contact)=>{
    //     text+='<a href="#" class="list-group-item list-group-action">${contact.name}</a>'

    // });


    // text+='</div>'

    let text = `<div class='list-group'>`;
data.forEach((contact) => {
    text += `<a href="/user/${contact.cId}/contact/" class='list-group-item list-group-item-action'>${contact.name}</a>`;
});
text += `</div>`;


    $(".search-result").html(text);
    $(".search-result").show();
});


        $(".search-result").show();
    }
}

