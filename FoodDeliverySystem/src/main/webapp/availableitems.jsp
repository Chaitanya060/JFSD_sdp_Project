<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="./css/home.css">
    <link rel="stylesheet" href="./styles/navbar.css">
    <link rel="stylesheet" href="./styles/footer.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
      integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link rel="stylesheet" href="./css/navbar.css">
    <link rel="stylesheet" href="./css/myorders.css"> 
    <title>Document</title>
</head>
<body>
    <%@include file="navbar.jsp" %>
    <div class="container">
        <h2 class="text-center section-head">Popular Foods</h2>
        <div class="row row-cols-1 row-cols-md-4 g-3">
            <% int idx=0; %>
            <c:forEach items="${items}" var="item" varStatus="loop">
                <div class="col">
                    <div class="my-card h-100" >
                        <img src="displayprodimage?id=${item.id}" class="card-img-top" alt="Product Image">
                        <div class="card-body">
                            <h5 class="card-title">${item.name}</h5>
                            <p class="card-text"><span style="color: red;">${item.price}</span>/5</p>
                            <button class="" onclick="placeOrder('${item.id}')">Order Now</button>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        function placeOrder(id) {
            $.ajax({
                type: "POST",
                url: "/placeorder/" + id,
                success: function(data) {
                    alert("Order placed successfully!");
                    // Optionally, you can update the page with the response data
                },
                error: function() {
                    alert("Error placing the order.");
                }
            });
        }
    </script>
    <%@include file="footer.jsp" %>
</body>
</html>
