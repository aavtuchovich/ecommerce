LOAD ={
    loadOrderData : function(){

        var formattedorderListArray =[];

        $.ajax({

            async: false,

            // url: "StudentJsonDataServlet",
            url: "/JsonDataOrderReport",

            dataType:"json",

            success: function(orderJsonData) {

                console.log(orderJsonData);

                $.each(orderJsonData,function(index,order){
                    formattedorderListArray.push([order[0],order[1]]);
                });
            }
        });
        return formattedorderListArray;
    }
};


window.onload = function () {

//Better to construct options first and then pass it as a parameter
    var datapoint = LOAD.loadOrderData();
    var options = {
        title: {
            text: "Spline Chart with Export as Image"
        },
        animationEnabled: true,
        exportEnabled: true,
        data: [
            {
                type: "spline", //change it to line, area, column, pie, etc
                dataPoints: datapoint
            }
        ]
    };
    $("#chartContainer").CanvasJSChart(options);

}