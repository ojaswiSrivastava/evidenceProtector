<?php
  if($_SERVER['REQUEST_METHOD']=='POST')
    {
      $firstname = $_POST['firstname'];
      $lastname = $_POST['lastname'];
      $username = $_POST['username'];
      $password = $_POST['password'];

      if($firstname == '' || $lastname == '' || $username == '' || $password == '')
        {
          echo 'please fill all values';
        }
      else
        {
          require_once('dbConnect.php');
          $sql = "SELECT * FROM profiles WHERE username='$username'";
 
          $check = mysqli_fetch_array(mysqli_query($con,$sql));
 
          if(isset($check))
            {
              echo 'username already taken, please select different username';
            }
              else
                { 
                  $sql = "INSERT INTO profiles(firstname,lastname,username,password) VALUES('$firstname','$lastname','$username','$password')";
                  if(mysqli_query($con,$sql))
                    {
                      echo 'successfully registered';
                    }
                  else
                    {
                      echo 'oops! Please try again!';
                    }
                }
     
           mysqli_close($con);
        }
    }
  
?>