'use strict';

const React = require('react');
const ReactDOM = require('react-dom')
const when = require('when');
const client = require('./client');
const follow = require('./follow');

const root = '/api/v1';

class App extends React.Component {

	constructor(props) {
        super(props);
        this.state = {items: []};
    }

    loadFromServer() {
    	follow(client, root, [
    		{rel: 'items'}]
    	).then(itemCollection => {
    		return client({
    			method: 'GET',
    			path: itemCollection.entity._links.profile.href,
    			headers: {'Accept': 'application/schema+json'}
    		}).then(schema => {
    			this.schema = schema.entity;
    			return itemCollection;
    		});
    	}).done(itemCollection => {
    		this.setState({
    			items: itemCollection.entity._embedded.items,
    			attributes: Object.keys(this.schema.properties),
    			links: itemCollection.entity._links});
    	});
    }

    componentDidMount() {
        this.loadFromServer();
    }

    render() {
        return (
            <ItemList items={this.state.items}/>
        )
    }
}

class ItemList extends React.Component{
	render() {
		var items = this.props.items.map(item =>
			<Item key={item._links.self.href} item={item}/>
		);
		return (
			<table>
				<tbody>
					<tr>
					    <th>Id</th>
						<th>Weight</th>
						<th>Price</th>
					</tr>
					{items}
				</tbody>
			</table>
		)
	}
}

class Item extends React.Component{
	render() {
		return (
			<tr>
			    <td>{this.props.item.id}</td>
				<td>{this.props.item.weight}</td>
				<td>{this.props.item.price}</td>
			</tr>
		)
	}
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
)

