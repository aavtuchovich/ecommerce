LOAD = {
    loadOrderData: function () {

        var formattedorderListArray = [];

        $.ajax({

            async: false,

            // url: "StudentJsonDataServlet",
            url: "/JsonDataOrderReport",

            dataType: "json",

            success: function (orderJsonData) {

                console.log(orderJsonData);

                $.each(orderJsonData, function (index, order) {
                    var date = new Date(order[1]);
                    formattedorderListArray.push({y: order[0], x: order[1]});
                });
            }
        });
        return formattedorderListArray;
    }
};


window.onload = function () {

//Better to construct options first and then pass it as a parameter
    var datapoint = LOAD.loadOrderData();
    CanvasJS.addCultureInfo("ru",
        {
            decimalSeparator: ",",// Observe ToolTip Number Format
            digitGroupSeparator: ".", // Observe axisY labels
            months: ["Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"],
            shortMonths:["Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"]
});
    var chart = new CanvasJS.Chart("chartContainer", {
        animationEnabled: true,
        culture:  "ru",
        title: {
            text: "Отчёт о заказах за весь период"
        },
        axisX: {
            title: "Дата"
        },
        axisY: {
            title: "Количество"
        },
        data: [{
            type: "line",
            connectNullData: true,
            xValueType: "dateTime",
            xValueFormatString: "DD MMMM HH:mm",
            yValueFormatString: "#,##0.##\" \"",
            dataPoints: datapoint
        }]
    });
    chart.render();

}