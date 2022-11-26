import React from 'react';
import { Route, Routes } from 'react-router-dom'
import './App.css';
import RegistrationForm from './components/registrationForm/RegistrationForm';
import AuthorizationForm from './components/authorizationForm/AuthorizationForm';

function App() {
  return (
    <Routes>
      <Route
        path='/addUser'
        element={
          <AuthorizationForm />
        }
      />
      <Route
        path='/registration'
        element={
          <RegistrationForm />
        }
      />
        <Route
        path='/'
        element={
          <div>Main page</div>
        }
      />
    </Routes>
  );
}

export default App;
