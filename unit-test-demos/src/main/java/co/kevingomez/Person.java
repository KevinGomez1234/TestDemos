package co.kevingomez;

import java.util.List;

public class Person {
	String firstName;
	String lastName;
	int age;
	String dob;
	private CarService cars;
	public Person(CarService cars) {
		this.cars = cars;
	}
	
	public Person() {
		// TODO Auto-generated constructor stub
	}

	public List<String> getCars() {
		return cars.getCars();
	}

	public void setCars(CarService cars) {
		this.cars = cars;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	
}
