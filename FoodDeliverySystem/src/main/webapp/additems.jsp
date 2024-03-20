<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

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
        <h3 class="text-center"><u>Add Items</u></h3>

        <form action="${pageContext.request.contextPath}/insertitem" method="post" enctype="multipart/form-data">
            <div class="form-group row">
                <label for="category" class="col-sm-2 col-form-label">Category</label>
                <div class="col-sm-10">
                    <select name="category" class="form-control" required>
                        <option value="">---Select---</option>
                        <option value="Quick Eats">Quick Eats</option>
                        <option value="Chinese">Chinese</option>
                        <option value="Smoothies">Smoothies</option>
                        <option value="Ice Creams">Ice Creams</option>
                        <option value="Japanese">Japanese</option>
                    </select>
                </div>
            </div>

            <div class="form-group row">
                <label for="name" class="col-sm-2 col-form-label">Item Name</label>
                <div class="col-sm-10">
                    <input type="text" name="name" class="form-control" required>
                </div>
            </div>

            <div class="form-group row">
                <label for="description" class="col-sm-2 col-form-label">Description</label>
                <div class="col-sm-10">
                    <textarea name="description" class="form-control"></textarea>
                </div>
            </div>

            <div class="form-group row">
                <label for="price" class="col-sm-2 col-form-label">Price</label>
                <div class="col-sm-10">
                    <input type="number" name="price" class="form-control" step="0.01" required>
                </div>
            </div>

            <div class="form-group row">
                <label for="productimage" class="col-sm-2 col-form-label">Image</label>
                <div class="col-sm-10">
                    <input type="file" name="productimage" class="form-control-file" required>
                </div>
            </div>

            <div class="form-group row">
                <div class="col-sm-12 text-center">
                    <input type="submit" value="Add" class="btn btn-primary">
                </div>
            </div>
        </form>
    </div>

    <!-- Bootstrap JS and jQuery -->
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>

</body>

</html>
