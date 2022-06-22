package com.emp.vo;

import java.sql.Date;
import java.util.List;

public class EmpWhereVO {
	private int salary;
	private int deptId;
	private Date startDate;
	private Date endDate;
	private List<Integer> deptIdList;
	
	
	public List<Integer> getDeptIdList() {
		return deptIdList;
	}
	public void setDeptIdList(List<Integer> deptIdList) {
		this.deptIdList = deptIdList;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	public int getDeptId() {
		return deptId;
	}
	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}