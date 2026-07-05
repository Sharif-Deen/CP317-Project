const Button = (props) => {
    return(
        <button className={props.className} onClick={props.onClick} type={props.type}>
            {props.label}
        </button>
    )
}

export default Button