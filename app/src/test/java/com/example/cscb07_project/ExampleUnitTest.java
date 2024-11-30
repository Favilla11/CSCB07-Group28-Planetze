
package com.example.cscb07_project;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import android.text.Editable;
import android.widget.EditText;

@RunWith(MockitoJUnitRunner.class)

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Mock
    private LogInView view;

    @Mock
    private LogInModel model;

    private LogInPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new LogInPresenter(view);
        presenter.model = model; // assuming model is package-private
    }

    @Test
    public void validateCredentials_withEmptyEmail() {
        presenter.validateCredentials("", "password");
        verify(view).setEmailError("Email cannot be empty");
        verifyNoMoreInteractions(view, model);
    }

    @Test
    public void validateCredentials_withEmptyPassword() {
        presenter.validateCredentials("email", "");

        verify(view).setPasswordError("Password cannot be empty");
        verifyNoMoreInteractions(view, model);
    }

    @Test
    public void validateCredentials_withValidCredentials_shouldCallModelLogin() {
        String email = "email";
        String password = "password";

        presenter.validateCredentials(email, password);

        verify(view).showProgress();
        verify(model).login(eq(email), eq(password), any(LogInModel.OnLoginFinishedListener.class));
    }
}