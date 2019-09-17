import React, {Component} from "react";
import BookList from "./views/BookList/BookList";

class Main extends Component {

    render() {
        return (
            <React.Fragment>
                <div className="main-wrp">
                    <BookList/>
                </div>
            </React.Fragment>
        );
    }
}

export default Main;
