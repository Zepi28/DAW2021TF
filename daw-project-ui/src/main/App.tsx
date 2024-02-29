import { ReactNode, useContext, useEffect, useState } from 'react'
import { BrowserRouter as Router, Redirect, Route, Switch } from 'react-router-dom'
import logo from './logo.svg';
import './App.css';
import * as Home from './home/Home'
import * as UserSession from './login/UserSession'
import * as Login from './login/Page'
import * as API from './common/FetchUtils'
import * as Project from './projects/Page'
import { OfflinePage, InvalidCredentialsPage } from './error/ErrorPages'
import { EnsureCredentials } from './login/EnsureCredentials'

const API_BASE_URL = 'http://localhost:8080/api'
const HOME_URL = new URL(`${API_BASE_URL}`)

type RouterProps = {
  fetchHomeInfo: (homeUrl: URL, credentials?: UserSession.Credentials) => API.Request<Home.Info>
}

/**
 * The application's splash page, displayed during startup.
 * @returns The React Element used to render the page.
 */
 // eslint-disable-next-line
 function SplashPage() {
  return (
    <header className="App-header">
      <img src={logo} className="App-logo" alt="logo" />
      <h1>Loading ...</h1>
    </header>
  )
}

function PageRouter({fetchHomeInfo}: RouterProps) {
  const loginPageRoute = '/login'
  const projectsPageRoute = '/projects'

  const [homeInfo, setHomeInfo] = useState<API.FetchInfo<Home.Info>>({
    status: API.FetchState.NOT_READY
  })

  const [invalidCredentials, setInvalidCredentials] = useState<boolean>(false)
  const userSession = useContext(UserSession.Context)
    
  useEffect(() => {
    async function sendRequest(request: API.Request<Home.Info>) {
      try {
        const result: API.Result<Home.Info> = await request.send()
       // console.log("Status code: " + result.header.status)

        if (result.header.ok) {
          setHomeInfo({ status: API.FetchState.SUCCESS, result })
        }
        else {
          if (API.isServerError(result)) {
            setHomeInfo({ status: API.FetchState.ERROR, result })
          }
          if (API.isAuthorizationError(result)) {
            setHomeInfo({ status: API.FetchState.SUCCESS, result })
            setInvalidCredentials(true)
          } else {
            setHomeInfo({ status: API.FetchState.SUCCESS, result })
          }
        }
      }
      catch(reason) {
        setHomeInfo({ status: API.FetchState.ERROR })
      }
    }
    
    if (userSession && userSession.credentials) {
      const homeRequest = fetchHomeInfo(HOME_URL, userSession.credentials)
      sendRequest(homeRequest)
      return homeRequest.cancel
    }
  }, [userSession, userSession?.credentials, fetchHomeInfo, setHomeInfo])

  function renderContent(): ReactNode {
    let content: ReactNode | undefined = undefined
    // console.log("Status: " + homeInfo.status);

    switch (homeInfo.status) {
      case API.FetchState.NOT_READY: content = <SplashPage />; break
      case API.FetchState.ERROR: content = <OfflinePage />; break
      default: content = invalidCredentials ? <InvalidCredentialsPage /> : 
      <Project.Page apiBaseUrl={API_BASE_URL} service = {Project.createService(userSession?.credentials)}/>
    }
    return content
  }

  return (
    <Router>
      <Switch>
        <Route exact path={loginPageRoute}>
          <Login.Page redirectPath={projectsPageRoute} />
        </Route>
        <Route exact path={projectsPageRoute}>
          <EnsureCredentials loginPageRoute={loginPageRoute}>
            { renderContent() }
          </EnsureCredentials>
        </Route>
        <Route path="/">
          <Redirect to={projectsPageRoute} />
        </Route>
      </Switch>
    </Router>
  )
}

function App() {
  const [userCredentials, setUserCredentials] = useState<UserSession.Credentials | undefined>(undefined)
  const userSessionRepo = UserSession.createRepository()

  const currentSessionContext = {
    credentials: userCredentials,
    login: (username: string, password: string) => {
      setUserCredentials(userSessionRepo.login(username, password))
    },
    logout: () => { userSessionRepo.logout(); setUserCredentials(undefined) }
  }

  return (
    <div className="App">
      <UserSession.Context.Provider value={currentSessionContext}>
        <PageRouter fetchHomeInfo={Home.fetchInfo} />
      </UserSession.Context.Provider> 
    </div>
  )
}

export default App;
