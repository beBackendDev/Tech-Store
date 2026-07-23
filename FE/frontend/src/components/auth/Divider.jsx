import "./Divider.scss";

function Divider({

    text = "OR"

}){

    return(

        <div className="divider">

            <span></span>

            <p>{text}</p>

            <span></span>

        </div>

    );

}

export default Divider;