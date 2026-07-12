import "../styles/Logo.css"

const Logo = ({ light = false }) => {
    return(
        <div className="logo-container">
            <div className="logo-circle">LFS</div>
            <h1 className="logo-title">
                <span className={light ? "logo-laurier-light" : "logo-laurier"}>LAURIER </span>
                <span className="logo-foodservices">Food Services</span>
            </h1>
        </div>
    )
}

export default Logo