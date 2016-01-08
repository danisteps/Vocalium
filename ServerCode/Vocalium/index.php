<?php
	$post_type = "";

	if ($_SERVER["REQUEST_METHOD"] == "POST") {

		$post_type = test_input($_POST["post_type"]);
		if($post_type == "sound")
		{
			$current_id = get_current_id();

			$file_path = getcwd() . "\Sound\\";
			$teacher_id = test_input($_POST["teacher"]);
			$student_id = test_input($_POST["student"]);

			$file_path = $file_path . $teacher_id . "\\";

			if(!is_dir($file_path)) mkdir("$file_path", 0777);

			$file_path = $file_path . $student_id . "\\";

			if(!is_dir($file_path)) mkdir("$file_path", 0777);


		    $file_path = $file_path . $current_id . ".mp3";


		    if(move_uploaded_file($_FILES['file']['tmp_name'], $file_path)) {
		        echo $current_id;
		    } else{
		        echo "failed";
		    }
		}
		elseif ($post_type == "comment") {
			$current_id = get_comment_current_id();

			$file_path = getcwd() . "\Comment\\";
			$teacher_id = test_input($_POST["teacher"]);
			$student_id = test_input($_POST["student"]);

			$file_path = $file_path . $teacher_id . "\\";

			if(!is_dir($file_path)) mkdir("$file_path", 0777);

			$file_path = $file_path . $student_id . "\\";

			if(!is_dir($file_path)) mkdir("$file_path", 0777);


			$file_path = $file_path . $current_id . ".txt";

		    if(move_uploaded_file($_FILES['file']['tmp_name'], $file_path)) {
		        echo $current_id;
		    } else{
		        echo "failed";
		    }
		}
	}

	
	function get_current_id() {
		$id_file = fopen("index\\currentSoundId.txt", "r+");

		$current_id = fgets($id_file);
		$current_id = intval($current_id) + 1;

		rewind($id_file);

		fwrite($id_file, $current_id);

		fclose($id_file);
		return "" . $current_id;
	}
	function get_comment_current_id() {
		$id_file = fopen("index\\currentCommentId.txt", "r+");

		$current_id = fgets($id_file);
		$current_id = intval($current_id) + 1;

		rewind($id_file);

		fwrite($id_file, $current_id);

		fclose($id_file);
		return "" . $current_id;
	}

	function test_input($data) {
	  $data = trim($data);
	  $data = stripslashes($data);
	  $data = htmlspecialchars($data);
	  return $data;
	}
?>
