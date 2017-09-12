<?php if (!defined('BASEPATH')) exit('No direct script access allowed');

class File_model extends CI_Model {
	
	function __construct() {
		parent::__construct();
	}

	function get_root_dir($key = '') {
		$return_string = $this->config->item('public_upload_path');
		if (empty($key)) {
			$key = $this->session->userdata('current_path_key');
		}
		$return_string .= $key;
		return $return_string;
	}

	function get_folder_list($current_path = '') {
		$return_array = array();
		$folder_array_temp = directory_map($current_path);
		$return_array = $this->file_model->dir_map_sort($folder_array_temp);
		return $return_array;
	}

	function dir_map_sort($array = '') {
		if (empty($array)) {
			return array();
		}
		$dirs = array();
		$files = array();

		foreach ($array as $key => $val) {
			// if is directory
			if (is_array($val)) {
				// run dir array through function to sort subdirs and files
				// unless it's empty
				$dirs[str_replace('/', '', $key)] = (!empty($array)) ? $this->file_model->dir_map_sort($val) : $val;
			}
			else {
				// We don't need this since we are listing files by using the DB
				//$files[str_replace('/', '', $key)] = $val;
			}
		}
		// sort by key (dir name)
		ksort($dirs);
    	// sort by value (file name)
		natcasesort($files);

		// put the sorted arrays back together
    	// swap $dirs and $files if you'd rather have files listed first
		return array_merge($dirs, $files);
	}

	function get_mime_type($full_path = '', $mode = 0) {

		$mime_types = array(
			'txt' => 'text/plain',
			'htm' => 'text/html',
			'html' => 'text/html',
			'php' => 'text/html',
			'css' => 'text/css',
			'js' => 'application/javascript',
			'json' => 'application/json',
			'xml' => 'application/xml',
			'swf' => 'application/x-shockwave-flash',
			'flv' => 'video/x-flv',

        	// images
			'png' => 'image/png',
			'jpe' => 'image/jpeg',
			'jpeg' => 'image/jpeg',
			'jpg' => 'image/jpeg',
			'gif' => 'image/gif',
			'bmp' => 'image/bmp',
			'ico' => 'image/vnd.microsoft.icon',
			'tiff' => 'image/tiff',
			'tif' => 'image/tiff',
			'svg' => 'image/svg+xml',
			'svgz' => 'image/svg+xml',

        	// archives
			'zip' => 'application/zip',
			'rar' => 'application/x-rar-compressed',
			'exe' => 'application/x-msdownload',
			'msi' => 'application/x-msdownload',
			'cab' => 'application/vnd.ms-cab-compressed',

        	// audio/video
			'mp3' => 'audio/mpeg',
			'qt' => 'video/quicktime',
			'mov' => 'video/quicktime',

        	// adobe
			'pdf' => 'application/pdf',
			'psd' => 'image/vnd.adobe.photoshop',
			'ai' => 'application/postscript',
			'eps' => 'application/postscript',
			'ps' => 'application/postscript',

        	// ms office
			'doc' => 'application/msword',
			'rtf' => 'application/rtf',
			'xls' => 'application/vnd.ms-excel',
			'ppt' => 'application/vnd.ms-powerpoint',
			'docx' => 'application/msword',
			'xlsx' => 'application/vnd.ms-excel',
			'pptx' => 'application/vnd.ms-powerpoint',


        	// open office
			'odt' => 'application/vnd.oasis.opendocument.text',
			'ods' => 'application/vnd.oasis.opendocument.spreadsheet',
			);

		$ext = strtolower(pathinfo($full_path, PATHINFO_EXTENSION));

		if (function_exists('finfo_open') && $mode == 0) {
			$finfo = finfo_open(FILEINFO_MIME); 
			$mimetype = finfo_file($finfo, $full_path); 
			finfo_close($finfo); 
			return $mimetype; 
		}
		else if (function_exists('mime_content_type') && $mode == 0) { 
			$mimetype = mime_content_type($full_path); 
			return $mimetype; 
		}
		else if (array_key_exists($ext, $mime_types)) { 
			return $mime_types[$ext]; 
		}
		else { 
			return 'application/octet-stream'; 
		} 
	}
	
	function is_in_group($group_id = '') {
		$return_boolean = false;
		if (!empty($group_id)) {
			$user_id = $this->session->userdata('user_id');
			if (!empty($group_id)) {
				$query_string = "
				SELECT * FROM group_members
				WHERE group_id='$group_id'
				AND user_id='$user_id'";
				$query = $this->db->query($query_string);
				if ($query->num_rows() > 0) {
					$return_boolean = true;
				}
			}
		}
		return $return_boolean;
	}
}