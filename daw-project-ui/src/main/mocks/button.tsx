import * as UserSession from '../login/UserSession'

export function NextButton() {
    return (
        <div className="ui animated button">
        <div className="visible content">Next</div>
        <div className="hidden content">
            <i className="right arrow icon"></i>
        </div>
        </div>
    )
}

export function BackButton() {
    return (
        <div className="ui animated button">
        <div className="visible content">Back</div>
        <div className="hidden content">
            <i className="left arrow icon"></i>
        </div>
        </div>
    )
}

