const InputField = (props) => {
    return(
        <div className="input-field-wrapper">
            {props.label && <label>{props.label}</label>}
            <input type={props.type} placeholder={props.placeholder} onChange={props.onChange} value={props.value}></input>
        </div>
    )
}
export default InputField