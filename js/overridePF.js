PrimeFaces.widget.DataTable.prototype.toggleExpansion = (function() {
	var cached_function = PrimeFaces.widget.DataTable.prototype.toggleExpansion;
	return function() {
		var row = arguments[0].closest('tr');
		
		expanded = row.hasClass('ui-expanded-row')
		
		if (!expanded) {
			if (this.cfg.selectionMode === 'single') {
				this.unselectAllRows();
			}
			if (this.cfg.selectionMode) {
				this.selectRow(row, false);
			}
		}
		else{
			row.next().remove();	
		}
		
		var result = cached_function.apply(this, arguments);
		
		return result;
	};
})();