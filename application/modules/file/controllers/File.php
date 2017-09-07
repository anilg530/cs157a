<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class File extends CI_Controller {

	public function __construct() {
		parent::__construct();

		if (!isset($_SESSION)) {
			session_start(); 
		}
		
		$this->load->model('file/file_model');
	}

	function index() {
		$this->session->set_userdata('username', 'trinh');
		$this->load->view('file');
	}
}
?>