import { Link, useNavigate } from 'react-router-dom'
import axios from "axios";
import { useState } from "react";
import style from './registrationForm.module.css';

const RegistrationForm = () => {

  // использование навигации - переброс на другой url
  let navigate = useNavigate()

  const [loginError, setloginError] = useState('');  // ошибка в логине
  const [passwordError, setPasswordError] = useState('');  // ошибка в пароле

  // state для пользователя
  const [user, setUser] = useState({
    login: "",
    password: ""
  })

  // объект пользователя с логином и паролем
  const { login, password } = user

  // при изменении в полях логина и пароля изменяются поля в объекте
  const onInputChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value })
  }

  // обработка отправки формы
  const onSubmit = async (e) => {
    e.preventDefault();
    setloginError('') // обнуление полей ошибок
    setPasswordError('')
    await axios.post("http://localhost:8081/api/registration", user) // отправка запроса
      .then((response) => response.data)
      .then((data) => {
        if (data.fieldErrors) { // если есть ошибки
          data.fieldErrors.forEach(fieldError => {
            if (fieldError.field === 'login') {
              setloginError(fieldError.message);
            }
            if (fieldError.field === 'password') {
              setPasswordError(fieldError.message);
            }
          });
        } else { // если нет ошибок
          navigate('/');
        }
      })
      .catch((err) => err);
  }

  return (
    <div className={style["main-div"]}>

      <div>
        <form className={style["form-reg"]} method="POST" onSubmit={onSubmit}>

          <h3>Регистрация</h3>

          <div className={style["input-div"]}>
            <label>Введите логин</label><br />
            <input type="text"
              placeholder="Логин"
              id="login" name="login"
              value={login}
              onChange={(e) => onInputChange(e)} /><br />
            {
              loginError ? <span style={{ color: 'red', fontSize: '12px' }}>{loginError}</span> : ''
            }
          </div>

          <div className={style["input-div"]}>
            <label>Введите пароль</label><br />
            <input type="password"
              placeholder="Пароль"
              id="password" name="password"
              value={password}
              onChange={(e) => onInputChange(e)} /><br />
            {
              passwordError ? <span style={{ color: 'red', fontSize: '12px' }}>{passwordError}</span> : ''
            }
          </div>
          <button className={style["btn-submit"]} type="submit">Зарегистрироваться</button>
        </form>
      </div>

      <div className={style["blue-div"]}>
        <div className={style["computer-man"]}>
          <img src="/img/man.png" alt="man"></img>
          <div>
            <h2>Sign Up to name</h2>
            <p>Lorem Ipsum is simply </p>
          </div>
        </div>
      </div>

    </div>

  )
}

export default RegistrationForm