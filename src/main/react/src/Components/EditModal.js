import React, { Component } from 'react';
import {Select, Row, Col, Input, Modal, Button, Icon} from "antd";
import Utils from "../Utils/Utils";
const { Option } = Select;

class EditModal extends Component {

    state = {
        modalVisible: true
    };

    componentWillMount() {
        this.getAuthorsAndGenres();
    }

    getAuthorsAndGenres() {
        this.getAuthors();
        this.getGenres();
    }

    getAuthors() {
        Utils.getData("authors").then((rs) => {
            this.setState({authors: rs ? rs : []})
        });
    }

    getGenres() {
        Utils.getData("genres").then((rs) => {
            this.setState({genres: rs ? rs : []})
        });
    }

    handleOk = () => {
        const {completed, data} = this.props;
        const {val, selectedAuthor, selectedGenre} = this.state;

        const newTitle = val ? val : data.id;
        const newAuthor = selectedAuthor ? selectedAuthor : data.author.id;
        const newGenre = selectedGenre ? selectedGenre : data.genre.id;

        Utils.putData({
            id: data.id,
            title: newTitle,
            authorId: newAuthor,
            genreId: newGenre
        }).then((rs) => {
            if (rs === 200) {
                completed(undefined, true);
            }
        });

    };

    handleCancel = () => {
        const {completed} = this.props;
        completed();
    };

    updateInputValue(evt) {
        this.setState({
            val: evt.target.value
        });
    }

    changeAuthor = (author) => {
        this.setState({selectedAuthor: author})
    };

    changeGenre = (genre) => {
        this.setState({selectedGenre: genre})
    };

    anyPropertiesChanged = () => {
        const {selectedAuthor, selectedGenre, val} = this.state;
        const {data} = this.props;

        return (
            (val !== undefined && val !== data.title) ||
            (selectedAuthor !== undefined && selectedAuthor !== data.author.id) ||
            (selectedGenre !== undefined && selectedGenre !== data.genre.id)
        )
    };

    render() {
        const {val, authors, genres} = this.state;
        const {data} = this.props;

        return (
            <Modal
                title={'Book Id: ' + data.id}
                visible={true}
                onOk={this.handleOk}
                onCancel={this.handleCancel}
                footer={[
                    <Button key="back"
                            style={{fontSize: '12px'}}
                            onClick={this.handleCancel}>
                        <Icon type="rollback" />
                        Cancel
                    </Button>,
                    <Button key="apply"
                            type="primary"
                            style={{fontSize: '12px'}}
                            onClick={this.handleOk}
                            disabled={!this.anyPropertiesChanged()}>
                        <Icon type="check-circle-o" />
                        Apply
                    </Button>
                ]}
            >
                <Row >
                    <Col span={10}>
                        <Input placeholder={data.title}
                               value={val}
                               onChange={evt => this.updateInputValue(evt)}
                        />
                    </Col>
                    <Col span={6} offset={1}>
                        <Select defaultValue={data.author.name} style={{ width: 120 }} onChange={this.changeAuthor}>
                            {
                                authors && authors.length > 0 ?
                                    authors.map((content) => (
                                        <Option value={content.id} key={content.id}>
                                            {content.name}
                                        </Option>
                                    )) : undefined
                            }
                        </Select>
                    </Col>
                    <Col span={6} offset={1}>
                        <Select defaultValue={data.genre.title} style={{ width: 120 }} onChange={this.changeGenre}>
                            {
                                genres && genres.length > 0 ?
                                    genres.map((content) => (
                                        <Option value={content.id} key={content.id}>
                                            {content.title}
                                        </Option>
                                    )) : undefined
                            }
                        </Select>
                    </Col>
                </Row>
            </Modal>
        )
    }
}

export default EditModal;
