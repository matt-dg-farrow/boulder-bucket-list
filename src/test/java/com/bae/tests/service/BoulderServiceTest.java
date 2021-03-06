package com.bae.tests.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.bae.exceptions.BoulderNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.bae.exceptions.InvalidDatesException;
import com.bae.exceptions.InvalidGradeException;
import com.bae.exceptions.InvalidStatusException;
import com.bae.persistence.domain.Boulder;
import com.bae.persistence.repo.BoulderRepo;
import com.bae.service.BoulderService;
import com.bae.util.Grade;
import com.bae.util.Status;

@RunWith(SpringRunner.class)
public class BoulderServiceTest {

	@InjectMocks
	private BoulderService service;

	@Mock
	private BoulderRepo repo;

	private List<Boulder> boulderList;

	private Boulder testBoulder;

	private Boulder testBoulderWithID;

	final long id = 1L;

	final long badID = 2L;

	@Before
	public void init() {
		Date testAttemptDate = new Date(2001 - 1 - 1);
		Date testCompletionDate = new Date(2001 - 1 - 1);
		this.boulderList = new ArrayList<>();
		this.boulderList.add(testBoulder);
		this.testBoulder = new Boulder("testName", "testLocation", Grade._5A, Status.COMPLETED, testAttemptDate,
				testCompletionDate);
		this.testBoulderWithID = new Boulder(testBoulder.getName(), testBoulder.getLocation(), testBoulder.getGrade(),
				testBoulder.getStatus(), testBoulder.getAttemptDate(), testBoulder.getCompletionDate());
		this.testBoulderWithID.setId(id);
	}

	@Test
	public void getAllBouldersTest() {
		when(this.repo.findAll()).thenReturn(this.boulderList);

		assertEquals(this.boulderList, this.service.getAllBoulders());
	}

	@Test
	public void addBoulderTest() {
		when(this.repo.save(testBoulder)).thenReturn(testBoulderWithID);

		assertEquals(this.testBoulderWithID, this.service.addBoulder(testBoulder));

		verify(this.repo, times(1)).save(this.testBoulder);
	}

	@Test
	public void deleteBoulderTest() {
		when(this.repo.existsById(id)).thenReturn(true, false);

		this.service.deleteBoulder(id);

		verify(this.repo, times(1)).deleteById(id);
		verify(this.repo, times(1)).existsById(id);
	}

	@Test(expected = BoulderNotFoundException.class)
	public void deleteBoulderBadIDTest() {
		this.service.deleteBoulder(badID);
	}

	@Test
	public void findBoulderByIdTest() {
		when(this.repo.findById(this.id)).thenReturn(Optional.of(this.testBoulderWithID));

		assertEquals(this.testBoulderWithID, this.service.findBoulderById(this.id));

		verify(this.repo, times(1)).findById(this.id);
	}

	@Test
	public void updateBoulderTest() {
		Date newAttemptDate = new Date(2019 - 12 - 18);
		Date newCompletionDate = new Date(2019 - 12 - 18);
		Boulder newBoulder = new Boulder("Chris Rock", "Madagascar", Grade._6A, Status.COMPLETED, newAttemptDate,
				newCompletionDate);
		Boulder updatedBoulder = new Boulder(newBoulder.getName(), newBoulder.getLocation(), newBoulder.getGrade(),
				newBoulder.getStatus(), newBoulder.getAttemptDate(), newBoulder.getCompletionDate());
		updatedBoulder.setId(this.id);

		when(this.repo.findById(this.id)).thenReturn(Optional.of(this.testBoulderWithID));
		when(this.repo.save(updatedBoulder)).thenReturn(updatedBoulder);

		assertEquals(updatedBoulder, this.service.updateBoulder(newBoulder, this.id));

		verify(this.repo, times(1)).findById(1L);
		verify(this.repo, times(1)).save(updatedBoulder);
	}

	@Test
	public void nullAttemptDateTest() {
		this.testBoulderWithID.setAttemptDate(null);
		when(this.repo.save(testBoulder)).thenReturn(testBoulderWithID);
		try {
			this.service.addBoulder(testBoulderWithID);
		} catch (InvalidDatesException e) {
			return;
		}
		fail();
	}

	@Test(expected = InvalidDatesException.class)
	public void attemptAfterCompletionTest() {
		Date testBadAttemptDate = new Date(2002 - 1 - 1);
		this.testBoulderWithID.setAttemptDate(testBadAttemptDate);
		when(this.repo.save(testBoulder)).thenReturn(testBoulderWithID);

		this.service.addBoulder(testBoulderWithID);
	}

	@Test
	public void statusNotNullTest() {
		this.testBoulderWithID.setStatus(null);
		when(this.repo.save(testBoulder)).thenReturn(testBoulderWithID);
		try {
			this.service.addBoulder(testBoulderWithID);
		} catch (InvalidStatusException e) {
			return;
		}
		fail();
	}

	@Test
	public void gradeNotNullTest() {
		this.testBoulderWithID.setGrade(null);
		when(this.repo.save(testBoulder)).thenReturn(testBoulderWithID);
		try {
			this.service.addBoulder(testBoulderWithID);
		} catch (InvalidGradeException e) {
			return;
		}
		fail();
	}

}
