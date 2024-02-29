import './Page.css'
import { useContext, useEffect, useState, useRef } from 'react'
import { Credentials } from '../login/UserSession'
import { getService , Service } from './Service'
import * as UserSession from '../login/UserSession'
import { Project } from './Model'
import * as API from '../common/FetchUtils'
import * as Siren from '../common/Siren'
import { isEmpty, hasWhiteSpace } from '../common/StringUtils'


/**
 * Types used to represent the page's power state and power update state. 
 */
 type projectsUpdate = API.Request<Siren.Entity<Project>> 
 type projectsInfo = API.FetchInfo<Siren.Entity<Project>> 

type PageHeaderProps = {
  projects?: projectsInfo
}

type PageProps = {
  service: Service,
  apiBaseUrl: string
}
// eslint-disable-next-line
export function Page(props: PageProps) {

  const projectNameInputRef = useRef<HTMLInputElement>(null)
  const projectDescInputRef = useRef<HTMLInputElement>(null)

  type FormState = { projectNameOK: boolean, projectDescOK: boolean }
  const [formState] = useState<FormState | undefined>()
  
  const [projects, setProjects] = useState<projectsInfo | undefined>()
  const [projectsUpdate, setProjectsUpdate] = useState<projectsUpdate>()

  const userSession = useContext(UserSession.Context)

  function isFormOK() { 
    return formState?.projectDescOK && formState.projectNameOK
  }

  useEffect(() => { 

    console.log("// TODO: Rendering Projects List..")
    
  }, [projectsUpdate])
  

  useEffect(() => { 
    async function sendProjectRequest(request: projectsUpdate) {
      try {
        setProjects({ status: API.FetchState.NOT_READY })
        let result: API.Result<Siren.Entity<Project>> = await request.send()
        if (projectsUpdate) return
        
        setProjects({ 
          status: result.header.ok && result.body ? API.FetchState.SUCCESS : API.FetchState.ERROR
        })
      }
      catch(reason) {
        setProjects({ status: API.FetchState.ERROR })
      }
    }
  }, [props.service, projectsUpdate])


  return (
      !userSession?.credentials ? <> </> : 
      <>
        <LogOutButton {...userSession} />
        
  
        <CreateNewProjectForm />
        <Card />
        <LoadProjects />


      </> 


    )

  function CreateNewProjectForm(){
    return(
<div className="ui middle aligned center aligned grid" style={{ marginTop: 0 }}>
      <div className="column" style={{maxWidth: 350}}>
        <h2 className="ui header centered">
          <div className="content">Create a New Project!</div>
        </h2>
        <form className={`ui large form ${formState && !isFormOK() ? ' error' : ''}`}>
          <div className="ui segment">
            <div className={`field ${formState && !formState.projectNameOK ? 'error' : ''}`}>
              <div className="ui input left icon">
                <i className="angle double right icon"></i>
                <input type="text" name="Your Project Name" placeholder="Your Project Name" ref={projectNameInputRef} />
              </div>
            </div>
            <div className={`field ${formState && !formState.projectDescOK ? 'error' : ''}`}>
              <div className="ui input left icon">
                <i className="angle double right icon"></i>
                <input type="text" name="Your Project Description" placeholder="Your Project Description" ref={projectDescInputRef} />
              </div>
            </div>
            <div className="ui teal labeled icon button"
              style={{ textAlign: 'center'}} 
              onClick={() => { handleSubmit() }}>Create<i className="add icon"></i>
            </div>
          </div>
        </form>
      </div>
    </div>
    )
  }
      
    function handleSubmit(){
    const projectName = projectNameInputRef.current?.value
    const projectDesc = projectDescInputRef.current?.value

    const form: FormState = { 
      projectNameOK: projectName !== undefined && !isEmpty(projectName) && !hasWhiteSpace(projectName),
      projectDescOK: projectDesc !== undefined && !isEmpty(projectDesc) && !hasWhiteSpace(projectDesc)
    }

    if (!form.projectNameOK) { projectNameInputRef.current?.focus() }
    else if (!form.projectDescOK) { projectDescInputRef.current?.focus() }
    else {
     
      const serv = createService(userSession?.credentials)
      const p = {p_name: projectName, p_description: projectDesc}
      serv.createProject(p)

      
      const response = props.service.getAllProjects()
  }
}

function LoadProjects(){
  return (
    <div className="ui middle aligned center aligned grid">
      <div className="column">
        <h2 className="ui header centered">
          <div className="content">Your Projects</div>
        </h2>
      </div>
    </div>
  )
}

/**
 * The header of the Projects control page.
 * @param {PageHeaderProps} props - The props object.
 * @returns The React Element used to render the page's header.
 */
// eslint-disable-next-line
 function PageHeader(props: PageHeaderProps) {

  let projectName = props.projects?.result?.body?.properties?.p_name
  let projectDesc = props.projects?.result?.body?.properties?.p_description

  return (
      <div className="Control-header">
        <div className="ui massive floating message">
            <p> List of Projects </p>
            <p> Project Name: {projectName}  </p>
            <p> Project Description: {projectDesc} </p>
        </div>
      </div>
  )
}

function LogOutButton(userSession : UserSession.ContextType){
  return (
      <button className="ui animated button" 
        style={{ float: 'right'}} 
        onClick={() => { userSession?.logout() }}>
        <div className="visible content">Log Out</div>
        <div className="hidden content">
            <i className="sign-out icon"></i>
        </div>
      </button>
  )   
}
}
/**
 * A Component used to display and eventually edit a Project.
 */
 export function Card() {
  return (
    <div className="ui raised centered card">
      <div className="content">
    
      </div>
      <div className="extra content">

      </div>
    </div>
  )
}


export function createService(credentials?: Credentials): Service {
  return getService(credentials)
}