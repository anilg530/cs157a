<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class File extends CI_Controller {

	public function __construct() {
		parent::__construct();

		if (!isset($_SESSION)) {
			session_start(); 
		}
		
		$this->load->model('file/file_model');
		$this->load->helper('directory');
	}

	function index() {
		$this->session->set_userdata('username', 'trinh');
		$this->session->set_userdata('user_id', 1);
		$this->load->view('file');
	}

	function group_files($group_id = '') {
		$page_data = array();
		$page_data['group_id'] = $group_id;
		$is_in_group = $this->file_model->is_in_group($group_id);
		if (!$this->input->is_ajax_request()) {
			$page_data['is_in_group'] = $is_in_group;
			if ($is_in_group) {
				$current_path_key = 'group_files/'.$group_id.'/';
				$current_path = $this->file_model->get_root_dir($current_path_key);
				$page_data['current_path'] = $current_path;
				$this->session->set_userdata('current_path_key', $current_path_key);
				$this->session->set_userdata('current_path', $current_path);
			}
			$page_data['page_data'] = $page_data;
			$this->load->view('file_browser', $page_data);
		}
		else {
			$response = array();
			if ($is_in_group) {

			}
			else {
				$response['swal_error'] = 'You do not have access to this group\'s files. Please refresh the page.';
				$response['status'] = 'fail';
			}
			echo json_encode($response);
			return;
		}
	}

	function group_files_upload($debug = '') {

	}
}
?>