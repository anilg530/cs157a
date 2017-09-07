<?php if (!defined('BASEPATH')) exit('No direct script access allowed');

class Jennifer_module extends CI_Model {
	
	function __construct() {
		parent::__construct();
	}
	
	function get_users() {
		$query = "
		SELECT *
		FROM user
		";

		$result = $this->db->query($query);
		if ($result->num_rows() > 0) {
			return $result->result_array();
		}
		else {
			return array();
		}
	}

	function get_group_test() {
		$query = "
		SELECT *
		FROM `group_members`
		JOIN `group` ON `group_members`.`group_id`=`group`.`id`
		JOIN `user` ON `group_members`.`user_id`=`user`.`id`
		";

		// $this->db->select('*');
		// $this->db->from('group_members');
		// $this->db->join('group', 'group_members.group_id=group.id');
		// $this->db->join('user', 'group_members.user_id=user.id');
		// $result = $this->db->get();

		$result = $this->db->query($query);
		if ($result->num_rows() > 0) {
			log_message('error', $this->db->last_query());
			return $result->result_array();
		}
		else {
			return array();
		}
	}
}