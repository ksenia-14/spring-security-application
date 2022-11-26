import { Link, useNavigate } from 'react-router-dom'
import axios from "axios";
import { useState } from "react";
import style from './authorizationForm.module.css';

const AuthorizationForm = () => {

  let navigate = useNavigate()

  const [user, setUser] = useState({
    login: "",
    password: ""
  })

  const { login, password } = user

  const onInputChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value })
  }


  const onSubmit = async (e) => {
    e.preventDefault();
    await axios.post("http://localhost:8081/api/registration", user)
    navigate("/")
  }

  return (
    <div className={style["main-div"]}>

      <div>
        <form className={style["form-reg"]} id="stripe-login" method="POST" onSubmit={onSubmit}>

          <h3>Войти</h3>

          <div className={style["input-div"]}>
            <label>Введите логин</label><br />
            <input type="text"
              placeholder="Логин"
              id="login" name="login"
              value={login}
              onChange={(e) => onInputChange(e)} /><br />
            {/* {
              loginError ? <span style={{ color: 'red', fontSize: '12px' }}>{loginError}</span> : ''
            } */}
          </div>

          <div className={style["input-div"]}>
            <label>Введите пароль</label><br />
            <input type="password"
              placeholder="Пароль"
              id="password" name="password"
              value={password}
              onChange={(e) => onInputChange(e)} /><br />
            {/* {
              passwordError ? <span style={{ color: 'red', fontSize: '12px' }}>{passwordError}</span> : ''
            } */}
          </div>
          <button className={style["btn-submit"]} type="submit">Войти</button>
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

export default AuthorizationForm