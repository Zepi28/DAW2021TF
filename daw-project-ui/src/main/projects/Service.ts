import { Project } from './Model'
import { Credentials } from '../login/UserSession'
import * as Siren from '../common/Siren'
import * as Fetch from '../common/FetchUtils'
import { resolve } from 'q'

const PROJECTS_BASE_API = 'http://localhost:8080/projects'
const NEW_PATH = '/new'

/**
 * Contract to be supported by the service used by the ControlPage.
 */
 export interface Service {
  getAllProjects: () => Fetch.Request<Siren.Entity<Project>>
  createProject: (project : Project) => void
  deleteProject: (project : Project) => void
  updateProject: (project : Project) => void
}

/**
 * An implementation of the Projects Control Service.
 * @returns the newly instantiated service.
 */
 export function getService(credentials?: Credentials) : Service {
 
  return{

    getAllProjects: (): Fetch.Request<Siren.Entity<Project>> => {
      
      return Fetch.cancelableRequest<Siren.Entity<Project>>(new URL('locnalhost:8080/projects'), {
        headers: credentials ? { 'Authorization': `${credentials.type} ${credentials.content.value}` } : { },
      })
    },

    async createProject(project : Project) {

      let options = {
        headers: {
          'content-type' : 'application/json',
          'Authorization' : 'Basic ' + credentials?.content.value
        },
        method: 'POST',
        body: JSON.stringify(project)
      }

      return fetch(PROJECTS_BASE_API + NEW_PATH, options)
        .then(res => res.json())
        .catch(err => console.log(err))
    },

    async deleteProject (project : Project) {

      let options = {
        headers: {
          'content-type' : 'application/json',
          'Authorization' : 'Basic ' + credentials?.content.value
        },
        method: 'DELETE',
      }

      return fetch(PROJECTS_BASE_API + "/" + project.p_name, options)
        .then(res => res.json())
        .catch(err => console.log(err))
    },

    async updateProject(project : Project) {

      let options = {
        headers: {
          'content-type' : 'application/json',
          'Authorization' : 'Basic ' + credentials?.content.value
        },
        method: 'PUT',
        body: JSON.stringify(project)
      }

      return fetch(PROJECTS_BASE_API + "/" + project.p_name, options)
        .then(res => res.json())
        .catch(err => console.log(err))
    }
  }
}   

function getData(url : RequestInfo, options: any){
  return fetch(url, options)
  .then(res => 
      res.json())
  .catch(err => {
      console.log(err)
      throw err
  })
}