const InputField = ({label, className='', ...rest}) => {
    return(
        <div className={`input-field-wrapper ${className}`}>
            {label && <label>{label}</label>}
            <input {...rest}/>
        </div>
    )
}
export default InputField