package com.odde.bbuddy.budget.domain;

import com.google.common.collect.Lists;
import com.odde.bbuddy.budget.repo.BudgetRepo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by zbcjackson on 22/11/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class BudgetServiceTest {

    @Mock
    private BudgetRepo repository;

    @InjectMocks
    private BudgetService budgetService;

    @Test
    public void add_by_repository() throws Exception {
        BudgetRepo repository = mock(BudgetRepo.class);
        BudgetService budgetService = new BudgetService(repository);
        Budget budget = new Budget();

        budgetService.add(budget);

        verify(repository).save(budget);
    }

    @Test
    public void add_when_a_record_existed() throws Exception {
        BudgetRepo repository = mock(BudgetRepo.class);
        Budget oldBudget = new Budget();
        oldBudget.setId(1);
        oldBudget.setAmount(1000);
        oldBudget.setMonth("2017-10");
        when(repository.findByMonth("2017-10")).thenReturn(oldBudget);

        BudgetService budgetService = new BudgetService(repository);
        Budget budget = new Budget();
        budget.setAmount(2000);
        budget.setMonth("2017-10");

        budgetService.add(budget);

        ArgumentCaptor<Budget> captor = ArgumentCaptor.forClass(Budget.class);
        verify(repository).save(captor.capture());
        Budget savedBudget = captor.getValue();
        assertEquals(1, savedBudget.getId());
        assertEquals(2000, savedBudget.getAmount());
        assertEquals("2017-10", savedBudget.getMonth());
    }

    @Test
    public void list_all_budgets() {
        Budget oldBudget1 = new Budget(1, 1500, "2016-07");
        Budget oldBudget2 = new Budget(2, 1000, "2016-08");
        Budget oldBudget3 = new Budget(3, 1600, "2016-09");
        List<Budget> budgetList = Lists.newArrayList(oldBudget1, oldBudget3, oldBudget2);

        when(repository.findAll()).thenReturn(budgetList);

        List<Budget> savedBudgets = budgetService.list();

        Assert.assertEquals(3, savedBudgets.size());
        Assert.assertEquals(oldBudget1, savedBudgets.get(0));
        Assert.assertEquals(oldBudget2, savedBudgets.get(1));
        Assert.assertEquals(oldBudget3, savedBudgets.get(2));
    }
}