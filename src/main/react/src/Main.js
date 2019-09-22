import React, {Component} from "react";
import BookList from "./views/BookList/BookList";
import {Button, Col, Row} from "antd";
import Utils from "./Utils/Utils";

class Main extends Component {

    handleLogout() {
        Utils.logout();
    }

    render() {
        return (
            <React.Fragment>
                <div className="main-wrp">
                    <Row>
                        <Col span={2} offset={22}>
                            <Button type="primary" onClick={this.handleLogout} style={{ marginTop: '10px', marginRight: '10px'}}>Logout</Button>
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <BookList/>
                        </Col>
                    </Row>
                </div>
            </React.Fragment>
        );
    }
}

export default Main;
