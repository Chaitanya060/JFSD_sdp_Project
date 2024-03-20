<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Spring Boot Project</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    <style>
        /* Add any custom styles here */
    </style>
</head>

<body>

    <%@ include file="restnavbar.jsp" %>

    <div class="container my-5">
        <h3 class="text-center"><u>View All Products</u></h3>

        <div class="my-4">
            <input type="text" id="myInput" onkeyup="myFunction()" class="form-control" placeholder="Enter Product Name">
        </div>

        <table class="table table-striped table-bordered" id="myTable">
            <thead class="thead-dark">
                <tr>
                    <th>ID</th>
                    <th>Category</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Cost</th>
                    <th>Image</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${productlist}" var="product">
                    <tr>
                        <td><c:out value="${product.id}"></c:out></td>
                        <td><c:out value="${product.category}"></c:out></td>
                        <td><c:out value="${product.name}"></c:out></td>
                        <td><c:out value="${product.description}"></c:out></td>
                        <td><c:out value="${product.price}"></c:out></td>
                        <td>
                            <img src='displayprodimage?id=${product.id}' class="img-fluid" alt="Product Image">
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- Bootstrap JS and jQuery -->
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>

    <script>
        function myFunction() {
            var input, filter, table, tr, td, i, txtValue;
            input = document.getElementById("myInput");
            filter = input.value.toUpperCase();
            table = document.getElementById("myTable");
            tr = table.getElementsByTagName("tr");
            for (i = 0; i < tr.length; i++) {
                td = tr[i].getElementsByTagName("td")[2];
                if (td) {
                    txtValue = td.textContent || td.innerText;
                    if (txtValue.toUpperCase().indexOf(filter) > -1) {
                        tr[i].style.display = "";
                    } else {
                        tr[i].style.display = "none";
                    }
                }
            }
        }
    </script>

</body>

</html>
