PrimeFaces.widget.DataTable = null;
PrimeFaces.widget.DataTable = PrimeFaces.widget.DeferredWidget.extend({
    SORT_ORDER: {
        ASCENDING: 1,
        DESCENDING: -1,
        UNSORTED: 0
    },
    init: function (a) {
        this._super(a);
        this.thead = this.getThead();
        this.tbody = this.getTbody();
        this.tfoot = this.getTfoot();
        if (this.cfg.paginator) {
            this.bindPaginator()
        }
        this.bindSortEvents();
        if (this.cfg.rowHover) {
            this.setupRowHover()
        }
        if (this.cfg.selectionMode) {
            this.setupSelection()
        }
        if (this.cfg.filter) {
            this.setupFiltering()
        }
        if (this.cfg.expansion) {
            this.expansionProcess = [];
            this.bindExpansionEvents()
        }
        if (this.cfg.editable) {
            this.bindEditEvents()
        }
        if (this.cfg.draggableRows) {
            this.makeRowsDraggable()
        }
        if (this.cfg.reflow) {
            this.initReflow()
        }
        this.renderDeferred()
    },
    _render: function () {
        console.log("JS hack");

        if (this.cfg.scrollable) {
            this.setupScrolling()
        }
        if (this.cfg.resizableColumns) {
            this.setupResizableColumns()
        }
        if (this.cfg.draggableColumns) {
            this.setupDraggableColumns()
        }
        if (this.cfg.stickyHeader) {
            this.setupStickyHeader()
        }
    },
    getThead: function () {
        return $(this.jqId + "_head")
    },
    getTbody: function () {
        return $(this.jqId + "_data")
    },
    getTfoot: function () {
        return $(this.jqId + "_foot")
    },
    updateData: function (c, a) {
        var b = (a === undefined) ? true : a;
        if (b) {
            this.tbody.html(c)
        } else {
            this.tbody.append(c)
        }
        this.postUpdateData()
    },
    postUpdateData: function () {
        if (this.cfg.draggableRows) {
            this.makeRowsDraggable()
        }
        if (this.cfg.reflow) {
            this.initReflow()
        }
    },
    refresh: function (a) {
        this.columnWidthsFixed = false;
        this.init(a)
    },
    bindPaginator: function () {
        var a = this;
        this.cfg.paginator.paginate = function (b) {
            a.paginate(b)
        };
        this.paginator = new PrimeFaces.widget.Paginator(this.cfg.paginator);
        if (this.cfg.lazyCache) {
            this.fetchNextPage()
        }
    },
    bindSortEvents: function () {
        var f = this;
        this.cfg.tabindex = this.cfg.tabindex || "0";
        this.sortableColumns = this.thead.find("> tr > th.ui-sortable-column");
        this.sortableColumns.attr("tabindex", this.cfg.tabindex);
        this.ascMessage = PrimeFaces.getAriaLabel("datatable.sort.ASC");
        this.descMessage = PrimeFaces.getAriaLabel("datatable.sort.DESC");
        if (this.cfg.multiSort) {
            this.sortMeta = []
        }
        for (var c = 0; c < this.sortableColumns.length; c++) {
            var e = this.sortableColumns.eq(c),
                    g = e.children("span.ui-sortable-column-icon"),
                    d = null,
                    a = e.attr("aria-label");
            if (e.hasClass("ui-state-active")) {
                if (g.hasClass("ui-icon-triangle-1-n")) {
                    d = this.SORT_ORDER.ASCENDING;
                    e.attr("aria-sort", "ascending").attr("aria-label", this.getSortMessage(a, this.descMessage))
                } else {
                    d = this.SORT_ORDER.DESCENDING;
                    e.attr("aria-sort", "descending").attr("aria-label", this.getSortMessage(a, this.ascMessage))
                }
                if (f.cfg.multiSort) {
                    f.addSortMeta({
                        col: e.attr("id"),
                        order: d
                    })
                }
            } else {
                d = this.SORT_ORDER.UNSORTED;
                e.attr("aria-sort", "other").attr("aria-label", this.getSortMessage(a, this.ascMessage))
            }
            e.data("sortorder", d)
        }
        this.sortableColumns.on("mouseenter.dataTable", function () {
            var h = $(this);
            if (!h.hasClass("ui-state-active")) {
                h.addClass("ui-state-hover")
            }
        }).on("mouseleave.dataTable", function () {
            var h = $(this);
            if (!h.hasClass("ui-state-active")) {
                h.removeClass("ui-state-hover")
            }
        }).on("blur.dataTable", function () {
            $(this).removeClass("ui-state-focus")
        }).on("focus.dataTable", function () {
            $(this).addClass("ui-state-focus")
        }).on("keydown.dataTable", function (j) {
            var h = j.which,
                    i = $.ui.keyCode;
            if ((h === i.ENTER || h === i.NUMPAD_ENTER) && $(j.target).is(":not(:input)")) {
                $(this).trigger("click.dataTable", (j.metaKey || j.ctrlKey));
                j.preventDefault()
            }
        }).on("click.dataTable", function (l, j) {
            if (!f.shouldSort(l, this)) {
                return
            }
            PrimeFaces.clearSelection();
            var k = $(this),
                    h = k.data("sortorder"),
                    i = (h === f.SORT_ORDER.UNSORTED) ? f.SORT_ORDER.ASCENDING : -1 * h,
                    m = l.metaKey || l.ctrlKey || j;
            if (f.cfg.multiSort) {
                if (m) {
                    f.addSortMeta({
                        col: k.attr("id"),
                        order: i
                    });
                    f.sort(k, i, true)
                } else {
                    f.sortMeta = [];
                    f.addSortMeta({
                        col: k.attr("id"),
                        order: i
                    });
                    f.sort(k, i)
                }
            } else {
                f.sort(k, i)
            }
            if (f.cfg.scrollable) {
                $(PrimeFaces.escapeClientId(k.attr("id") + "_clone")).trigger("focus")
            }
        });
        var b = $(this.jqId + "_reflowDD");
        if (b && this.cfg.reflow) {
            PrimeFaces.skinSelect(b);
            b.change(function (j) {
                var k = $(this).val().split("_"),
                        i = f.sortableColumns.eq(parseInt(k[0])),
                        h = parseInt(k[1]);
                i.data("sortorder", h);
                i.trigger("click.dataTable")
            })
        }
    },
    getSortMessage: function (a, c) {
        var b = a ? a.split(":")[0] : "";
        return b + ": " + c
    },
    shouldSort: function (b, a) {
        if (this.isEmpty()) {
            return false
        }
        var c = $(b.target);
        if (c.closest(".ui-column-customfilter", a).length) {
            return false
        }
        return c.is("th,span")
    },
    addSortMeta: function (a) {
        this.sortMeta = $.grep(this.sortMeta, function (b) {
            return b.col !== a.col
        });
        this.sortMeta.push(a)
    },
    setupFiltering: function () {
        var b = this,
                a = this.thead.find("> tr > th.ui-filter-column");
        this.cfg.filterEvent = this.cfg.filterEvent || "keyup";
        this.cfg.filterDelay = this.cfg.filterDelay || 300;
        a.children(".ui-column-filter").each(function () {
            var c = $(this);
            if (c.is("input:text")) {
                PrimeFaces.skinInput(c);
                b.bindTextFilter(c)
            } else {
                PrimeFaces.skinSelect(c);
                b.bindChangeFilter(c)
            }
        })
    },
    bindTextFilter: function (a) {
        if (this.cfg.filterEvent === "enter") {
            this.bindEnterKeyFilter(a)
        } else {
            this.bindFilterEvent(a)
        }
    },
    bindChangeFilter: function (a) {
        var b = this;
        a.change(function () {
            b.filter()
        })
    },
    bindEnterKeyFilter: function (a) {
        var b = this;
        a.bind("keydown", function (f) {
            var c = f.which,
                    d = $.ui.keyCode;
            if ((c === d.ENTER || c === d.NUMPAD_ENTER)) {
                f.preventDefault()
            }
        }).bind("keyup", function (f) {
            var c = f.which,
                    d = $.ui.keyCode;
            if ((c === d.ENTER || c === d.NUMPAD_ENTER)) {
                b.filter();
                f.preventDefault()
            }
        })
    },
    bindFilterEvent: function (a) {
        var b = this;
        a.on("keydown.dataTable-blockenter", function (f) {
            var c = f.which,
                    d = $.ui.keyCode;
            if ((c === d.ENTER || c === d.NUMPAD_ENTER)) {
                f.preventDefault()
            }
        }).on(this.cfg.filterEvent + ".dataTable", function (c) {
            if (b.filterTimeout) {
                clearTimeout(b.filterTimeout)
            }
            b.filterTimeout = setTimeout(function () {
                b.filter();
                b.filterTimeout = null
            }, b.cfg.filterDelay)
        })
    },
    setupRowHover: function () {
        var a = "> tr.ui-widget-content";
        if (!this.cfg.selectionMode) {
            this.bindRowHover(a)
        }
    },
    setupSelection: function () {
        this.selectionHolder = this.jqId + "_selection";
        this.cfg.rowSelectMode = this.cfg.rowSelectMode || "new";
        this.rowSelector = "> tr.ui-widget-content.ui-datatable-selectable";
        this.cfg.disabledTextSelection = this.cfg.disabledTextSelection === false ? false : true;
        var a = $(this.selectionHolder).val();
        this.selection = (a === "") ? [] : a.split(",");
        this.originRowIndex = 0;
        this.cursorIndex = null;
        this.bindSelectionEvents()
    },
    bindSelectionEvents: function () {
        if (this.cfg.selectionMode === "radio") {
            this.bindRadioEvents()
        } else {
            if (this.cfg.selectionMode === "checkbox") {
                this.bindCheckboxEvents();
                this.updateHeaderCheckbox();
                if (this.cfg.rowSelectMode !== "checkbox") {
                    this.bindRowEvents()
                }
            } else {
                this.bindRowEvents()
            }
        }
    },
    bindRowEvents: function () {
        var a = this;
        this.bindRowHover(this.rowSelector);
        this.tbody.off("click.dataTable mousedown.dataTable", this.rowSelector).on("mousedown.dataTable", this.rowSelector, null, function (b) {
            a.mousedownOnRow = true
        }).on("click.dataTable", this.rowSelector, null, function (b) {
            a.onRowClick(b, this);
            a.mousedownOnRow = false
        });
        if (this.hasBehavior("rowDblselect")) {
            this.tbody.off("dblclick.dataTable", this.rowSelector).on("dblclick.dataTable", this.rowSelector, null, function (b) {
                a.onRowDblclick(b, $(this))
            })
        }
        this.bindSelectionKeyEvents()
    },
    bindSelectionKeyEvents: function () {
        var a = this;
        this.getFocusableTbody().on("focus", function (b) {
            if (!a.mousedownOnRow) {
                a.focusedRow = a.tbody.children("tr.ui-widget-content.ui-datatable-selectable").eq(0);
                a.highlightFocusedRow();
                if (a.cfg.scrollable) {
                    PrimeFaces.scrollInView(a.scrollBody, a.focusedRow)
                }
            }
        }).on("blur", function () {
            if (a.focusedRow) {
                a.unhighlightFocusedRow();
                a.focusedRow = null
            }
        }).on("keydown", function (f) {
            var d = $.ui.keyCode,
                    b = f.which;
            if (a.focusedRow) {
                switch (b) {
                    case d.UP:
                        var g = a.focusedRow.prev("tr.ui-widget-content.ui-datatable-selectable");
                        if (g.length) {
                            a.unhighlightFocusedRow();
                            a.focusedRow = g;
                            a.highlightFocusedRow();
                            if (a.cfg.scrollable) {
                                PrimeFaces.scrollInView(a.scrollBody, a.focusedRow)
                            }
                        }
                        f.preventDefault();
                        break;
                    case d.DOWN:
                        var c = a.focusedRow.next("tr.ui-widget-content.ui-datatable-selectable");
                        if (c.length) {
                            a.unhighlightFocusedRow();
                            a.focusedRow = c;
                            a.highlightFocusedRow();
                            if (a.cfg.scrollable) {
                                PrimeFaces.scrollInView(a.scrollBody, a.focusedRow)
                            }
                        }
                        f.preventDefault();
                        break;
                        f.target = a.focusedRow.children().eq(0).get(0);
                        a.onRowClick(f, a.focusedRow.get(0));
                        f.preventDefault();
                        break;
                    default:
                        break
                }
            }
        })
    },
    highlightFocusedRow: function () {
        this.focusedRow.addClass("ui-state-hover")
    },
    unhighlightFocusedRow: function () {
        this.focusedRow.removeClass("ui-state-hover")
    },
    assignFocusedRow: function (a) {
        this.focusedRow = a
    },
    bindRowHover: function (a) {
        this.tbody.off("mouseenter.dataTable mouseleave.dataTable", a).on("mouseenter.dataTable", a, null, function () {
            var b = $(this);
            if (!b.hasClass("ui-state-highlight")) {
                b.addClass("ui-state-hover")
            }
        }).on("mouseleave.dataTable", a, null, function () {
            var b = $(this);
            if (!b.hasClass("ui-state-highlight")) {
                b.removeClass("ui-state-hover")
            }
        })
    },
    bindRadioEvents: function () {
        var c = this,
                b = "> tr.ui-widget-content:not(.ui-datatable-empty-message) > td.ui-selection-column :radio";
        if (this.cfg.nativeElements) {
            this.tbody.off("click.dataTable", b).on("click.dataTable", b, null, function (f) {
                var d = $(this);
                if (!d.prop("checked")) {
                    c.selectRowWithRadio(d)
                }
            })
        } else {
            var a = "> tr.ui-widget-content:not(.ui-datatable-empty-message) > td.ui-selection-column .ui-radiobutton .ui-radiobutton-box";
            this.tbody.off("click.dataTable mouseover.dataTable mouseout.dataTable", a).on("mouseover.dataTable", a, null, function () {
                var d = $(this);
                if (!d.hasClass("ui-state-disabled") && !d.hasClass("ui-state-active")) {
                    d.addClass("ui-state-hover")
                }
            }).on("mouseout.dataTable", a, null, function () {
                var d = $(this);
                d.removeClass("ui-state-hover")
            }).on("click.dataTable", a, null, function () {
                var d = $(this),
                        f = d.hasClass("ui-state-active"),
                        e = d.hasClass("ui-state-disabled");
                if (!e && !f) {
                    c.selectRowWithRadio(d)
                }
            })
        }
        this.tbody.off("focus.dataTable blur.dataTable change.dataTable", b).on("focus.dataTable", b, null, function () {
            var d = $(this),
                    e = d.parent().next();
            if (d.prop("checked")) {
                e.removeClass("ui-state-active")
            }
            e.addClass("ui-state-focus")
        }).on("blur.dataTable", b, null, function () {
            var d = $(this),
                    e = d.parent().next();
            if (d.prop("checked")) {
                e.addClass("ui-state-active")
            }
            e.removeClass("ui-state-focus")
        }).on("change.dataTable", b, null, function () {
            var d = c.tbody.find(b).filter(":checked"),
                    e = d.parent().next();
            c.selectRowWithRadio(e)
        })
    },
    bindCheckboxEvents: function () {
        var b = this,
                c = "> tr.ui-widget-content.ui-datatable-selectable > td.ui-selection-column :checkbox";
        if (this.cfg.nativeElements) {
            this.checkAllToggler = this.thead.find("> tr > th.ui-selection-column > :checkbox");
            this.checkAllTogglerInput = this.checkAllToggler;
            this.checkAllToggler.on("click", function () {
                b.toggleCheckAll()
            });
            this.tbody.off("click.dataTable", c).on("click.dataTable", c, null, function (f) {
                var d = $(this);
                if (d.prop("checked")) {
                    b.selectRowWithCheckbox(d)
                } else {
                    b.unselectRowWithCheckbox(d)
                }
            })
        } else {
            this.checkAllToggler = this.thead.find("> tr > th.ui-selection-column > .ui-chkbox.ui-chkbox-all > .ui-chkbox-box");
            this.checkAllTogglerInput = this.checkAllToggler.prev().children(":checkbox");
            this.checkAllToggler.on("mouseover", function () {
                var d = $(this);
                if (!d.hasClass("ui-state-disabled") && !d.hasClass("ui-state-active")) {
                    d.addClass("ui-state-hover")
                }
            }).on("mouseout", function () {
                $(this).removeClass("ui-state-hover")
            }).on("click", function () {
                var d = $(this);
                if (!d.hasClass("ui-state-disabled")) {
                    b.toggleCheckAll()
                }
            });
            var a = "> tr.ui-widget-content.ui-datatable-selectable > td.ui-selection-column .ui-chkbox .ui-chkbox-box";
            this.tbody.off("mouseover.dataTable mouseover.dataTable click.dataTable", a).on("mouseover.dataTable", a, null, function () {
                var d = $(this);
                if (!d.hasClass("ui-state-active")) {
                    d.addClass("ui-state-hover")
                }
            }).on("mouseout.dataTable", a, null, function () {
                $(this).removeClass("ui-state-hover")
            }).on("click.dataTable", a, null, function () {
                var e = $(this),
                        d = e.hasClass("ui-state-active");
                if (d) {
                    b.unselectRowWithCheckbox(e)
                } else {
                    b.selectRowWithCheckbox(e)
                }
            })
        }
        this.tbody.off("focus.dataTable blur.dataTable change.dataTable", c).on("focus.dataTable", c, null, function () {
            var d = $(this),
                    e = d.parent().next();
            if (d.prop("checked")) {
                e.removeClass("ui-state-active")
            }
            e.addClass("ui-state-focus")
        }).on("blur.dataTable", c, null, function () {
            var d = $(this),
                    e = d.parent().next();
            if (d.prop("checked")) {
                e.addClass("ui-state-active")
            }
            e.removeClass("ui-state-focus")
        }).on("change.dataTable", c, null, function (g) {
            var d = $(this),
                    f = d.parent().next();
            if (d.prop("checked")) {
                b.selectRowWithCheckbox(f)
            } else {
                b.unselectRowWithCheckbox(f)
            }
        });
        this.checkAllTogglerInput.on("focus.dataTable", function (g) {
            var d = $(this),
                    f = d.parent().next();
            if (!f.hasClass("ui-state-disabled")) {
                if (d.prop("checked")) {
                    f.removeClass("ui-state-active")
                }
                f.addClass("ui-state-focus")
            }
        }).on("blur.dataTable", function (g) {
            var d = $(this),
                    f = d.parent().next();
            if (d.prop("checked")) {
                f.addClass("ui-state-active")
            }
            f.removeClass("ui-state-focus")
        }).on("change.dataTable", function (g) {
            var d = $(this),
                    f = d.parent().next();
            if (!f.hasClass("ui-state-disabled")) {
                if (!d.prop("checked")) {
                    f.addClass("ui-state-active")
                }
                b.toggleCheckAll();
                if (d.prop("checked")) {
                    f.removeClass("ui-state-active").addClass("ui-state-focus")
                }
            }
        })
    },
    bindExpansionEvents: function () {
        var b = this,
                a = "> tr > td > div.ui-row-toggler";
        this.tbody.off("click.datatable-expansion", a).on("click.datatable-expansion", a, null, function () {
            b.toggleExpansion($(this))
        }).on("keydown.datatable-expansion", a, null, function (f) {
            var c = f.which,
                    d = $.ui.keyCode;
            if ((c === d.ENTER || c === d.NUMPAD_ENTER)) {
                b.toggleExpansion($(this));
                f.preventDefault()
            }
        })
    },
    bindContextMenu: function (e, f, b, a) {
        var d = b + " tbody.ui-datatable-data > tr.ui-widget-content";
        var c = a.event + ".datatable";
        $(document).off(c, d).on(c, d, null, function (i) {
            var j = $(this);
            if (f.cfg.selectionMode && j.hasClass("ui-datatable-selectable")) {
                f.onRowRightClick(i, this, a.selectionMode);
                e.show(i)
            } else {
                if (f.cfg.editMode === "cell") {
                    var h = $(i.target),
                            g = h.is("td.ui-editable-column") ? h : h.parents("td.ui-editable-column:first");
                    if (f.contextMenuCell) {
                        f.contextMenuCell.removeClass("ui-state-highlight")
                    }
                    f.contextMenuClick = true;
                    f.contextMenuCell = g;
                    f.contextMenuCell.addClass("ui-state-highlight");
                    e.show(i)
                } else {
                    if (j.hasClass("ui-datatable-empty-message")) {
                        e.show(i)
                    }
                }
            }
        })
    },
    initReflow: function () {
        var a = this.thead.find("> tr > th");
        for (var b = 0; b < a.length; b++) {
            var c = a.eq(b),
                    d = c.children(".ui-column-title").text();
            this.tbody.find("> tr:not(.ui-datatable-empty-message) > td:nth-child(" + (b + 1) + ")").prepend('<span class="ui-column-title">' + d + "</span>")
        }
    },
    setupScrolling: function () {
        this.scrollHeader = this.jq.children(".ui-datatable-scrollable-header");
        this.scrollBody = this.jq.children(".ui-datatable-scrollable-body");
        this.scrollFooter = this.jq.children(".ui-datatable-scrollable-footer");
        this.scrollStateHolder = $(this.jqId + "_scrollState");
        this.scrollHeaderBox = this.scrollHeader.children("div.ui-datatable-scrollable-header-box");
        this.scrollFooterBox = this.scrollFooter.children("div.ui-datatable-scrollable-footer-box");
        this.headerTable = this.scrollHeaderBox.children("table");
        this.bodyTable = this.scrollBody.children("table");
        this.footerTable = this.scrollFooter.children("table");
        this.footerCols = this.scrollFooter.find("> .ui-datatable-scrollable-footer-box > table > tfoot > tr > td");
        this.percentageScrollHeight = this.cfg.scrollHeight && (this.cfg.scrollHeight.indexOf("%") !== -1);
        this.percentageScrollWidth = this.cfg.scrollWidth && (this.cfg.scrollWidth.indexOf("%") !== -1);
        var c = this,
                b = this.getScrollbarWidth() + "px";
        if (this.cfg.scrollHeight) {
            if (this.percentageScrollHeight) {
                this.adjustScrollHeight()
            }
            if (this.hasVerticalOverflow()) {
                this.scrollHeaderBox.css("margin-right", b);
                this.scrollFooterBox.css("margin-right", b)
            }
        }
        this.fixColumnWidths();
        if (this.cfg.scrollWidth) {
            if (this.percentageScrollWidth) {
                this.adjustScrollWidth()
            } else {
                this.setScrollWidth(parseInt(this.cfg.scrollWidth))
            }
        }
        this.cloneHead();
        this.restoreScrollState();
        if (this.cfg.liveScroll) {
            this.scrollOffset = 0;
            this.cfg.liveScrollBuffer = (100 - this.cfg.liveScrollBuffer) / 100;
            this.shouldLiveScroll = true;
            this.loadingLiveScroll = false;
            this.allLoadedLiveScroll = c.cfg.scrollStep >= c.cfg.scrollLimit
        }
        this.scrollBody.on("scroll.dataTable", function () {
            var g = c.scrollBody.scrollLeft();
            c.scrollHeaderBox.css("margin-left", -g);
            c.scrollFooterBox.css("margin-left", -g);
            if (c.shouldLiveScroll) {
                var f = Math.ceil(this.scrollTop),
                        e = this.scrollHeight,
                        d = this.clientHeight;
                if ((f >= ((e * c.cfg.liveScrollBuffer) - (d))) && c.shouldLoadLiveScroll()) {
                    c.loadLiveRows()
                }
            }
            c.saveScrollState()
        });
        this.scrollHeader.on("scroll.dataTable", function () {
            c.scrollHeader.scrollLeft(0)
        });
        this.scrollFooter.on("scroll.dataTable", function () {
            c.scrollFooter.scrollLeft(0)
        });
        var a = "resize." + this.id;
        $(window).unbind(a).bind(a, function () {
            if (c.jq.is(":visible")) {
                if (c.percentageScrollHeight) {
                    c.adjustScrollHeight()
                }
                if (c.percentageScrollWidth) {
                    c.adjustScrollWidth()
                }
            }
        })
    },
    shouldLoadLiveScroll: function () {
        return (!this.loadingLiveScroll && !this.allLoadedLiveScroll)
    },
    cloneHead: function () {
        this.theadClone = this.thead.clone();
        this.theadClone.find("th").each(function () {
            var b = $(this);
            b.attr("id", b.attr("id") + "_clone");
            $(this).children().not(".ui-column-title").remove()
        });
        this.theadClone.removeAttr("id").addClass("ui-datatable-scrollable-theadclone").height(0).prependTo(this.bodyTable);
        if (this.sortableColumns.length) {
            this.sortableColumns.removeAttr("tabindex").off("blur.dataTable focus.dataTable keydown.dataTable");
            var a = this.theadClone.find("> tr > th.ui-sortable-column");
            a.each(function () {
                $(this).data("original", $(this).attr("id").split("_clone")[0])
            });
            a.on("blur.dataTable", function () {
                $(PrimeFaces.escapeClientId($(this).data("original"))).removeClass("ui-state-focus")
            }).on("focus.dataTable", function () {
                $(PrimeFaces.escapeClientId($(this).data("original"))).addClass("ui-state-focus")
            }).on("keydown.dataTable", function (d) {
                var b = d.which,
                        c = $.ui.keyCode;
                if ((b === c.ENTER || b === c.NUMPAD_ENTER) && $(d.target).is(":not(:input)")) {
                    $(PrimeFaces.escapeClientId($(this).data("original"))).trigger("click.dataTable", (d.metaKey || d.ctrlKey));
                    d.preventDefault()
                }
            })
        }
    },
    adjustScrollHeight: function () {
        var d = this.jq.parent().innerHeight() * (parseInt(this.cfg.scrollHeight) / 100),
                f = this.jq.children(".ui-datatable-header").outerHeight(true),
                b = this.jq.children(".ui-datatable-footer").outerHeight(true),
                c = (this.scrollHeader.outerHeight(true) + this.scrollFooter.outerHeight(true)),
                e = this.paginator ? this.paginator.getContainerHeight(true) : 0,
                a = (d - (c + e + f + b));
        this.scrollBody.height(a)
    },
    adjustScrollWidth: function () {
        var a = parseInt((this.jq.parent().innerWidth() * (parseInt(this.cfg.scrollWidth) / 100)));
        this.setScrollWidth(a)
    },
    setOuterWidth: function (a, b) {
        var c = a.outerWidth() - a.width();
        a.width(b - c)
    },
    setScrollWidth: function (a) {
        var b = this;
        this.jq.children(".ui-widget-header").each(function () {
            b.setOuterWidth($(this), a)
        });
        this.scrollHeader.width(a);
        this.scrollBody.css("margin-right", 0).width(a);
        this.scrollFooter.width(a)
    },
    alignScrollBody: function () {
        var a = this.hasVerticalOverflow() ? this.getScrollbarWidth() + "px" : "0px";
        this.scrollHeaderBox.css("margin-right", a);
        this.scrollFooterBox.css("margin-right", a)
    },
    getScrollbarWidth: function () {
        if (!this.scrollbarWidth) {
            this.scrollbarWidth = PrimeFaces.env.browser.webkit ? "15" : PrimeFaces.calculateScrollbarWidth()
        }
        return this.scrollbarWidth
    },
    hasVerticalOverflow: function () {
        return (this.cfg.scrollHeight && this.bodyTable.outerHeight() > this.scrollBody.outerHeight())
    },
    restoreScrollState: function () {
        var a = this.scrollStateHolder.val(),
                b = a.split(",");
        this.scrollBody.scrollLeft(b[0]);
        this.scrollBody.scrollTop(b[1])
    },
    saveScrollState: function () {
        var a = this.scrollBody.scrollLeft() + "," + this.scrollBody.scrollTop();
        this.scrollStateHolder.val(a)
    },
    clearScrollState: function () {
        this.scrollStateHolder.val("0,0")
    },
    fixColumnWidths: function () {
        var d = this;
        if (!this.columnWidthsFixed) {
            if (PrimeFaces.isIE(7)) {
                this.bodyTable.css("width", "auto")
            }
            if (this.cfg.scrollable) {
                this.scrollHeader.find("> .ui-datatable-scrollable-header-box > table > thead > tr > th").each(function () {
                    var h = $(this),
                            e = h.index(),
                            f = h.width();
                    h.width(f);
                    if (d.footerCols.length > 0) {
                        var g = d.footerCols.eq(e);
                        g.width(f)
                    }
                })
            } else {
                var b = this.jq.find("> .ui-datatable-tablewrapper > table > thead > tr > th"),
                        a = b.filter(":visible"),
                        c = b.filter(":hidden");
                this.setColumnsWidth(a);
                this.setColumnsWidth(c)
            }
            this.columnWidthsFixed = true
        }
    },
    setColumnsWidth: function (a) {
        if (a.length) {
            a.each(function () {
                var b = $(this);
                b.width(b.width())
            })
        }
    },
    loadLiveRows: function () {
        if (this.liveScrollActive || (this.scrollOffset + this.cfg.scrollStep > this.cfg.scrollLimit)) {
            return
        }
        this.liveScrollActive = true;
        this.scrollOffset += this.cfg.scrollStep;
        if (this.scrollOffset === this.cfg.scrollLimit) {
            this.shouldLiveScroll = false
        }
        var b = this,
                a = {
                    source: this.id,
                    process: this.id,
                    update: this.id,
                    formId: this.cfg.formId,
                    params: [{
                            name: this.id + "_scrolling",
                            value: true
                        }, {
                            name: this.id + "_skipChildren",
                            value: true
                        }, {
                            name: this.id + "_scrollOffset",
                            value: this.scrollOffset
                        }, {
                            name: this.id + "_encodeFeature",
                            value: true
                        }],
                    onsuccess: function (e, c, d) {
                        PrimeFaces.ajax.Response.handle(e, c, d, {
                            widget: b,
                            handle: function (f) {
                                this.updateData(f, false);
                                this.liveScrollActive = false
                            }
                        });
                        return true
                    },
                    oncomplete: function (e, c, d) {
                        if (d.totalRecords) {
                            b.cfg.scrollLimit = d.totalRecords
                        }
                        b.loadingLiveScroll = false;
                        b.allLoadedLiveScroll = (b.scrollOffset + b.cfg.scrollStep) >= b.cfg.scrollLimit
                    }
                };
        PrimeFaces.ajax.Request.handle(a)
    },
    paginate: function (d) {
        var c = this,
                b = {
                    source: this.id,
                    update: this.id,
                    process: this.id,
                    formId: this.cfg.formId,
                    params: [{
                            name: this.id + "_pagination",
                            value: true
                        }, {
                            name: this.id + "_first",
                            value: d.first
                        }, {
                            name: this.id + "_rows",
                            value: d.rows
                        }, {
                            name: this.id + "_skipChildren",
                            value: true
                        }, {
                            name: this.id + "_encodeFeature",
                            value: true
                        }],
                    onsuccess: function (g, e, f) {
                        PrimeFaces.ajax.Response.handle(g, e, f, {
                            widget: c,
                            handle: function (h) {
                                this.updateData(h);
                                if (this.checkAllToggler) {
                                    this.updateHeaderCheckbox()
                                }
                                if (this.cfg.scrollable) {
                                    this.alignScrollBody()
                                }
                            }
                        });
                        return true
                    },
                    oncomplete: function (g, e, f) {
                        c.paginator.cfg.page = d.page;
                        if (f && f.totalRecords) {
                            c.paginator.updateTotalRecords(f.totalRecords)
                        } else {
                            c.paginator.updateUI()
                        }
                        if (c.cfg.lazyCache) {
                            c.fetchNextPage()
                        }
                    }
                };
        if (this.hasBehavior("page")) {
            var a = this.cfg.behaviors.page;
            a.call(this, b)
        } else {
            PrimeFaces.ajax.Request.handle(b)
        }
    },
    fetchNextPage: function () {
        var b = this,
                a = {
                    source: this.id,
                    process: this.id,
                    update: this.id,
                    params: [{
                            name: this.id + "_pagination",
                            value: true
                        }, {
                            name: this.id + "_loadlazycache",
                            value: true
                        }, {
                            name: this.id + "_encodeFeature",
                            value: true
                        }],
                    onsuccess: function (e, c, d) {
                        PrimeFaces.ajax.Response.handle(e, c, d, {
                            widget: b,
                            handle: function (f) {}
                        });
                        return true
                    }
                };
        PrimeFaces.ajax.Request.handle(a)
    },
    sort: function (d, a, f) {
        var e = this,
                b = {
                    source: this.id,
                    update: this.id,
                    process: this.id,
                    params: [{
                            name: this.id + "_sorting",
                            value: true
                        }, {
                            name: this.id + "_skipChildren",
                            value: true
                        }, {
                            name: this.id + "_encodeFeature",
                            value: true
                        }],
                    onsuccess: function (i, g, h) {
                        PrimeFaces.ajax.Response.handle(i, g, h, {
                            widget: e,
                            handle: function (j) {
                                this.updateData(j)
                            }
                        });
                        return true
                    },
                    oncomplete: function (q, j, m) {
                        var p = e.getPaginator();
                        if (p && m && p.cfg.rowCount !== m.totalRecords) {
                            p.setTotalRecords(m.totalRecords)
                        }
                        if (!m.validationFailed) {
                            if (p) {
                                p.setPage(0, true)
                            }
                            if (!f) {
                                var g = e.sortableColumns.filter(".ui-state-active");
                                for (var k = 0; k < g.length; k++) {
                                    var h = $(g.get(k)),
                                            n = h.attr("aria-label");
                                    h.attr("aria-sort", "other").attr("aria-label", e.getSortMessage(n, e.ascMessage));
                                    $(PrimeFaces.escapeClientId(h.attr("id") + "_clone")).attr("aria-sort", "other").attr("aria-label", e.getSortMessage(n, e.ascMessage))
                                }
                                g.data("sortorder", e.SORT_ORDER.UNSORTED).removeClass("ui-state-active").find(".ui-sortable-column-icon").removeClass("ui-icon-triangle-1-n ui-icon-triangle-1-s")
                            }
                            d.data("sortorder", a).removeClass("ui-state-hover").addClass("ui-state-active");
                            var l = d.find(".ui-sortable-column-icon"),
                                    o = d.attr("aria-label");
                            if (a === e.SORT_ORDER.DESCENDING) {
                                l.removeClass("ui-icon-triangle-1-n").addClass("ui-icon-triangle-1-s");
                                d.attr("aria-sort", "descending").attr("aria-label", e.getSortMessage(o, e.ascMessage));
                                $(PrimeFaces.escapeClientId(d.attr("id") + "_clone")).attr("aria-sort", "descending").attr("aria-label", e.getSortMessage(o, e.ascMessage))
                            } else {
                                if (a === e.SORT_ORDER.ASCENDING) {
                                    l.removeClass("ui-icon-triangle-1-s").addClass("ui-icon-triangle-1-n");
                                    d.attr("aria-sort", "ascending").attr("aria-label", e.getSortMessage(o, e.descMessage));
                                    $(PrimeFaces.escapeClientId(d.attr("id") + "_clone")).attr("aria-sort", "ascending").attr("aria-label", e.getSortMessage(o, e.descMessage))
                                }
                            }
                        }
                    }
                };
        if (f) {
            b.params.push({
                name: this.id + "_multiSorting",
                value: true
            });
            b.params.push({
                name: this.id + "_sortKey",
                value: e.joinSortMetaOption("col")
            });
            b.params.push({
                name: this.id + "_sortDir",
                value: e.joinSortMetaOption("order")
            })
        } else {
            b.params.push({
                name: this.id + "_sortKey",
                value: d.attr("id")
            });
            b.params.push({
                name: this.id + "_sortDir",
                value: a
            })
        }
        if (this.hasBehavior("sort")) {
            var c = this.cfg.behaviors.sort;
            c.call(this, b)
        } else {
            PrimeFaces.ajax.Request.handle(b)
        }
    },
    joinSortMetaOption: function (b) {
        var c = "";
        for (var a = 0; a < this.sortMeta.length; a++) {
            c += this.sortMeta[a][b];
            if (a !== (this.sortMeta.length - 1)) {
                c += ","
            }
        }
        return c
    },
    filter: function () {
        var c = this,
                a = {
                    source: this.id,
                    update: this.id,
                    process: this.id,
                    formId: this.cfg.formId,
                    params: [{
                            name: this.id + "_filtering",
                            value: true
                        }, {
                            name: this.id + "_encodeFeature",
                            value: true
                        }],
                    onsuccess: function (f, d, e) {
                        PrimeFaces.ajax.Response.handle(f, d, e, {
                            widget: c,
                            handle: function (g) {
                                this.updateData(g);
                                if (this.cfg.scrollable) {
                                    this.alignScrollBody()
                                }
                                if (this.isCheckboxSelectionEnabled()) {
                                    this.updateHeaderCheckbox()
                                }
                            }
                        });
                        return true
                    },
                    oncomplete: function (f, d, e) {
                        var g = c.getPaginator();
                        if (g) {
                            g.setTotalRecords(e.totalRecords)
                        }
                    }
                };
        if (this.hasBehavior("filter")) {
            var b = this.cfg.behaviors.filter;
            b.call(this, a)
        } else {
            PrimeFaces.ajax.AjaxRequest(a)
        }
    },
    onRowClick: function (e, d, a) {
        if ($(e.target).is("td:not(.ui-column-unselectable),span:not(.ui-c)")) {
            var g = $(d),
                    c = g.hasClass("ui-state-highlight"),
                    f = e.metaKey || e.ctrlKey,
                    b = e.shiftKey;
            this.assignFocusedRow(g);
            if (c && f) {
                this.unselectRow(g, a)
            } else {
                if (this.isSingleSelection() || (this.isMultipleSelection() && e && !f && !b && this.cfg.rowSelectMode === "new")) {
                    this.unselectAllRows()
                }
                if (this.isMultipleSelection() && e && e.shiftKey) {
                    this.selectRowsInRange(g)
                } else {
                    this.originRowIndex = g.index();
                    this.cursorIndex = null;
                    this.selectRow(g, a)
                }
            }
            if (this.cfg.disabledTextSelection) {
                PrimeFaces.clearSelection()
            }
        }
    },
    onRowDblclick: function (a, c) {
        if (this.cfg.disabledTextSelection) {
            PrimeFaces.clearSelection()
        }
        if ($(a.target).is("td,span:not(.ui-c)")) {
            var b = this.getRowMeta(c);
            this.fireRowSelectEvent(b.key, "rowDblselect")
        }
    },
    onRowRightClick: function (c, b, f) {
        var e = $(b),
                d = this.getRowMeta(e),
                a = e.hasClass("ui-state-highlight");
        if (f === "single" || !a) {
            this.unselectAllRows()
        }
        this.selectRow(e, true);
        this.fireRowSelectEvent(d.key, "contextMenu");
        if (this.cfg.disabledTextSelection) {
            PrimeFaces.clearSelection()
        }
    },
    findRow: function (a) {
        var b = a;
        if (PrimeFaces.isNumber(a)) {
            b = this.tbody.children("tr:eq(" + a + ")")
        }
        return b
    },
    selectRowsInRange: function (f) {
        var c = this.tbody.children(),
                e = this.getRowMeta(f),
                d = this;
        if (this.cursorIndex !== null) {
            var g = this.cursorIndex,
                    a = g > this.originRowIndex ? c.slice(this.originRowIndex, g + 1) : c.slice(g, this.originRowIndex + 1);
            a.each(function (h, j) {
                d.unselectRow($(j), true)
            })
        }
        this.cursorIndex = f.index();
        var b = this.cursorIndex > this.originRowIndex ? c.slice(this.originRowIndex, this.cursorIndex + 1) : c.slice(this.cursorIndex, this.originRowIndex + 1);
        b.each(function (h, j) {
            d.selectRow($(j), true)
        });
        this.fireRowSelectEvent(e.key, "rowSelect")
    },
    selectRow: function (b, a) {
        var d = this.findRow(b),
                c = this.getRowMeta(d);
        this.highlightRow(d);
        if (this.isCheckboxSelectionEnabled()) {
            if (this.cfg.nativeElements) {
                d.children("td.ui-selection-column").find(":checkbox").prop("checked", true)
            } else {
                this.selectCheckbox(d.children("td.ui-selection-column").find("> div.ui-chkbox > div.ui-chkbox-box"))
            }
            this.updateHeaderCheckbox()
        }
        this.addSelection(c.key);
        this.writeSelections();
        if (!a) {
            this.fireRowSelectEvent(c.key, "rowSelect")
        }
    },
    unselectRow: function (b, a) {
        var d = this.findRow(b),
                c = this.getRowMeta(d);
        this.unhighlightRow(d);
        if (this.isCheckboxSelectionEnabled()) {
            if (this.cfg.nativeElements) {
                d.children("td.ui-selection-column").find(":checkbox").prop("checked", false)
            } else {
                this.unselectCheckbox(d.children("td.ui-selection-column").find("> div.ui-chkbox > div.ui-chkbox-box"))
            }
            this.updateHeaderCheckbox()
        }
        this.removeSelection(c.key);
        this.writeSelections();
        if (!a) {
            this.fireRowUnselectEvent(c.key, "rowUnselect")
        }
    },
    highlightRow: function (a) {
        a.removeClass("ui-state-hover").addClass("ui-state-highlight").attr("aria-selected", true)
    },
    unhighlightRow: function (a) {
        a.removeClass("ui-state-highlight").attr("aria-selected", false)
    },
    fireRowSelectEvent: function (d, a) {
        if (this.cfg.behaviors) {
            var c = this.cfg.behaviors[a];
            if (c) {
                var b = {
                    params: [{
                            name: this.id + "_instantSelectedRowKey",
                            value: d
                        }]
                };
                c.call(this, b)
            }
        }
    },
    fireRowUnselectEvent: function (d, b) {
        if (this.cfg.behaviors) {
            var a = this.cfg.behaviors[b];
            if (a) {
                var c = {
                    params: [{
                            name: this.id + "_instantUnselectedRowKey",
                            value: d
                        }]
                };
                a.call(this, c)
            }
        }
    },
    selectRowWithRadio: function (a) {
        var c = a.closest("tr"),
                b = this.getRowMeta(c);
        this.unselectAllRows();
        if (!this.cfg.nativeElements) {
            this.selectRadio(a)
        }
        this.highlightRow(c);
        this.addSelection(b.key);
        this.writeSelections();
        this.fireRowSelectEvent(b.key, "rowSelectRadio")
    },
    selectRowWithCheckbox: function (b, a) {
        var d = b.closest("tr"),
                c = this.getRowMeta(d);
        this.highlightRow(d);
        if (!this.cfg.nativeElements) {
            this.selectCheckbox(b)
        }
        this.addSelection(c.key);
        this.writeSelections();
        if (!a) {
            this.updateHeaderCheckbox();
            this.fireRowSelectEvent(c.key, "rowSelectCheckbox")
        }
    },
    unselectRowWithCheckbox: function (b, a) {
        var d = b.closest("tr"),
                c = this.getRowMeta(d);
        this.unhighlightRow(d);
        if (!this.cfg.nativeElements) {
            this.unselectCheckbox(b)
        }
        this.removeSelection(c.key);
        this.uncheckHeaderCheckbox();
        this.writeSelections();
        if (!a) {
            this.fireRowUnselectEvent(c.key, "rowUnselectCheckbox")
        }
    },
    unselectAllRows: function () {
        var c = this.tbody.children("tr.ui-state-highlight"),
                a = this.isCheckboxSelectionEnabled(),
                e = this.isRadioSelectionEnabled();
        for (var b = 0; b < c.length; b++) {
            var d = c.eq(b);
            this.unhighlightRow(d);
            if (a) {
                if (this.cfg.nativeElements) {
                    d.children("td.ui-selection-column").find(":checkbox").prop("checked", false)
                } else {
                    this.unselectCheckbox(d.children("td.ui-selection-column").find("> div.ui-chkbox > div.ui-chkbox-box"))
                }
            } else {
                if (e) {
                    if (this.cfg.nativeElements) {
                        d.children("td.ui-selection-column").find(":radio").prop("checked", false)
                    } else {
                        this.unselectRadio(d.children("td.ui-selection-column").find("> div.ui-radiobutton > div.ui-radiobutton-box"))
                    }
                }
            }
        }
        if (a) {
            this.uncheckHeaderCheckbox()
        }
        this.selection = [];
        this.writeSelections()
    },
    selectAllRowsOnPage: function () {
        var b = this.tbody.children("tr");
        for (var a = 0; a < b.length; a++) {
            var c = b.eq(a);
            this.selectRow(c, true)
        }
    },
    unselectAllRowsOnPage: function () {
        var b = this.tbody.children("tr");
        for (var a = 0; a < b.length; a++) {
            var c = b.eq(a);
            this.unselectRow(c, true)
        }
    },
    selectAllRows: function () {
        this.selectAllRowsOnPage();
        this.selection = new Array("@all");
        this.writeSelections()
    },
    toggleCheckAll: function () {
        if (this.cfg.nativeElements) {
            var d = this.tbody.find("> tr.ui-datatable-selectable > td.ui-selection-column > :checkbox"),
                    c = this.checkAllToggler.prop("checked"),
                    e = this;
            d.each(function () {
                if (c) {
                    var f = $(this);
                    f.prop("checked", true);
                    e.selectRowWithCheckbox(f, true)
                } else {
                    var f = $(this);
                    f.prop("checked", false);
                    e.unselectRowWithCheckbox(f, true)
                }
            })
        } else {
            var d = this.tbody.find("> tr.ui-datatable-selectable > td.ui-selection-column .ui-chkbox-box"),
                    c = this.checkAllToggler.hasClass("ui-state-active"),
                    e = this;
            if (c) {
                this.checkAllToggler.removeClass("ui-state-active").children("span.ui-chkbox-icon").addClass("ui-icon-blank").removeClass("ui-icon-check");
                this.checkAllTogglerInput.prop("checked", false).attr("aria-checked", false);
                d.each(function () {
                    e.unselectRowWithCheckbox($(this), true)
                })
            } else {
                this.checkAllToggler.addClass("ui-state-active").children("span.ui-chkbox-icon").removeClass("ui-icon-blank").addClass("ui-icon-check");
                this.checkAllTogglerInput.prop("checked", true).attr("aria-checked", true);
                d.each(function () {
                    e.selectRowWithCheckbox($(this), true)
                })
            }
        }
        this.writeSelections();
        if (this.cfg.behaviors) {
            var a = this.cfg.behaviors.toggleSelect;
            if (a) {
                var b = {
                    params: [{
                            name: this.id + "_checked",
                            value: !c
                        }]
                };
                a.call(this, b)
            }
        }
    },
    selectCheckbox: function (a) {
        if (!a.hasClass("ui-state-focus")) {
            a.addClass("ui-state-active")
        }
        a.children("span.ui-chkbox-icon:first").removeClass("ui-icon-blank").addClass(" ui-icon-check");
        a.prev().children("input").prop("checked", true).attr("aria-checked", true)
    },
    unselectCheckbox: function (a) {
        a.removeClass("ui-state-active");
        a.children("span.ui-chkbox-icon:first").addClass("ui-icon-blank").removeClass("ui-icon-check");
        a.prev().children("input").prop("checked", false).attr("aria-checked", false)
    },
    selectRadio: function (a) {
        a.removeClass("ui-state-hover");
        if (!a.hasClass("ui-state-focus")) {
            a.addClass("ui-state-active")
        }
        a.children(".ui-radiobutton-icon").addClass("ui-icon-bullet").removeClass("ui-icon-blank");
        a.prev().children("input").prop("checked", true)
    },
    unselectRadio: function (a) {
        a.removeClass("ui-state-active").children(".ui-radiobutton-icon").addClass("ui-icon-blank").removeClass("ui-icon-bullet");
        a.prev().children("input").prop("checked", false)
    },
    toggleExpansion: function (b) {
        var d = b.closest("tr"),
                g = this.getRowMeta(d).index,
                f = b.hasClass("ui-icon"),
                e = b.children("span"),
                a = f ? b.hasClass("ui-icon-circle-triangle-s") : b.children("span").eq(0).hasClass("ui-helper-hidden"),
                c = this;
        if ($.inArray(g, this.expansionProcess) === -1) {
            this.expansionProcess.push(g);
            if (a) {
                if (f) {
                    b.addClass("ui-icon-circle-triangle-e").removeClass("ui-icon-circle-triangle-s").attr("aria-expanded", false)
                } else {
                    e.eq(0).removeClass("ui-helper-hidden");
                    e.eq(1).addClass("ui-helper-hidden")
                }
                this.collapseRow(d);
                c.expansionProcess = $.grep(c.expansionProcess, function (h) {
                    return (h !== g)
                });
                this.fireRowCollapseEvent(d)
            } else {
                if (this.cfg.rowExpandMode === "single") {
                    this.collapseAllRows()
                }
                if (f) {
                    b.addClass("ui-icon-circle-triangle-s").removeClass("ui-icon-circle-triangle-e").attr("aria-expanded", true)
                } else {
                    e.eq(0).addClass("ui-helper-hidden");
                    e.eq(1).removeClass("ui-helper-hidden")
                }
                this.loadExpandedRowContent(d)
            }
        }
    },
    loadExpandedRowContent: function (d) {
        var c = this,
                e = this.getRowMeta(d).index,
                a = {
                    source: this.id,
                    process: this.id,
                    update: this.id,
                    formId: this.cfg.formId,
                    params: [{
                            name: this.id + "_rowExpansion",
                            value: true
                        }, {
                            name: this.id + "_expandedRowIndex",
                            value: e
                        }, {
                            name: this.id + "_encodeFeature",
                            value: true
                        }, {
                            name: this.id + "_skipChildren",
                            value: true
                        }],
                    onsuccess: function (h, f, g) {
                        PrimeFaces.ajax.Response.handle(h, f, g, {
                            widget: c,
                            handle: function (i) {
                                if (i && $.trim(i).length) {
                                    d.addClass("ui-expanded-row");
                                    this.displayExpandedRow(d, i)
                                }
                            }
                        });
                        return true
                    },
                    oncomplete: function () {
                        c.expansionProcess = $.grep(c.expansionProcess, function (f) {
                            return f !== e
                        })
                    }
                };
        if (this.hasBehavior("rowToggle")) {
            var b = this.cfg.behaviors.rowToggle;
            b.call(this, a)
        } else {
            PrimeFaces.ajax.AjaxRequest(a)
        }
    },
    displayExpandedRow: function (b, a) {
        b.after(a)
    },
    fireRowCollapseEvent: function (c) {
        var d = this.getRowMeta(c).index;
        if (this.hasBehavior("rowToggle")) {
            var a = {
                params: [{
                        name: this.id + "_collapsedRowIndex",
                        value: d
                    }]
            };
            var b = this.cfg.behaviors.rowToggle;
            b.call(this, a)
        }
    },
    collapseRow: function (a) {
        a.removeClass("ui-expanded-row").next(".ui-expanded-row-content").remove()
    },
    collapseAllRows: function () {
        var a = this;
        this.getExpandedRows().each(function () {
            var f = $(this);
            a.collapseRow(f);
            var c = f.children("td");
            for (var b = 0; b < c.length; b++) {
                var d = c.eq(b),
                        e = d.children(".ui-row-toggler");
                if (e.length > 0) {
                    if (e.hasClass("ui-icon")) {
                        e.addClass("ui-icon-circle-triangle-e").removeClass("ui-icon-circle-triangle-s")
                    } else {
                        var g = e.children("span");
                        g.eq(0).removeClass("ui-helper-hidden");
                        g.eq(1).addClass("ui-helper-hidden")
                    }
                    break
                }
            }
        })
    },
    getExpandedRows: function () {
        return this.tbody.children(".ui-expanded-row")
    },
    bindEditEvents: function () {
        var c = this;
        this.cfg.cellSeparator = this.cfg.cellSeparator || " ";
        this.cfg.saveOnCellBlur = (this.cfg.saveOnCellBlur === false) ? false : true;
        if (this.cfg.editMode === "row") {
            var a = "> tr > td > div.ui-row-editor";
            this.tbody.off("click.datatable", a).on("click.datatable", a, null, function (f) {
                var d = $(f.target),
                        g = d.closest("tr");
                if (d.hasClass("ui-icon-pencil")) {
                    c.switchToRowEdit(g);
                    d.hide().siblings().show()
                } else {
                    if (d.hasClass("ui-icon-check")) {
                        c.saveRowEdit(g)
                    } else {
                        if (d.hasClass("ui-icon-close")) {
                            c.cancelRowEdit(g)
                        }
                    }
                }
            })
        } else {
            if (this.cfg.editMode === "cell") {
                var b = "> tr > td.ui-editable-column";
                this.tbody.off("click.datatable-cell", b).on("click.datatable-cell", b, null, function (f) {
                    c.incellClick = true;
                    var d = $(this);
                    if (!d.hasClass("ui-cell-editing")) {
                        c.showCellEditor($(this))
                    }
                });
                $(document).off("click.datatable-cell-blur" + this.id).on("click.datatable-cell-blur" + this.id, function (d) {
                    if (!c.incellClick && c.currentCell && !c.contextMenuClick && !$.datepicker._datepickerShowing) {
                        if (c.cfg.saveOnCellBlur) {
                            c.saveCell(c.currentCell)
                        } else {
                            c.doCellEditCancelRequest(c.currentCell)
                        }
                    }
                    c.incellClick = false;
                    c.contextMenuClick = false
                })
            }
        }
    },
    switchToRowEdit: function (c) {
        this.showRowEditors(c);
        if (this.hasBehavior("rowEditInit")) {
            var b = this.cfg.behaviors.rowEditInit,
                    d = this.getRowMeta(c).index;
            var a = {
                params: [{
                        name: this.id + "_rowEditIndex",
                        value: d
                    }]
            };
            b.call(this, a)
        }
    },
    showRowEditors: function (a) {
        a.addClass("ui-state-highlight ui-row-editing").children("td.ui-editable-column").each(function () {
            var b = $(this);
            b.find(".ui-cell-editor-output").hide();
            b.find(".ui-cell-editor-input").show()
        })
    },
    showCellEditor: function (g) {
        this.incellClick = true;
        var k = null,
                h = this;
        if (g) {
            k = g;
            if (this.contextMenuCell) {
                this.contextMenuCell.parent().removeClass("ui-state-highlight")
            }
        } else {
            k = this.contextMenuCell
        }
        if (this.currentCell) {
            if (this.cfg.saveOnCellBlur) {
                this.saveCell(this.currentCell)
            } else {
                this.doCellEditCancelRequest(this.currentCell)
            }
        }
        this.currentCell = k;
        var b = k.children("div.ui-cell-editor"),
                a = b.children("div.ui-cell-editor-output"),
                l = b.children("div.ui-cell-editor-input"),
                e = l.find(":input:enabled"),
                f = e.length > 1;
        k.addClass("ui-state-highlight ui-cell-editing");
        a.hide();
        l.show();
        e.eq(0).focus().select();
        if (f) {
            var j = [];
            for (var d = 0; d < e.length; d++) {
                j.push(e.eq(d).val())
            }
            k.data("multi-edit", true);
            k.data("old-value", j)
        } else {
            k.data("multi-edit", false);
            k.data("old-value", e.eq(0).val())
        }
        if (!k.data("edit-events-bound")) {
            k.data("edit-events-bound", true);
            e.on("keydown.datatable-cell", function (o) {
                var n = $.ui.keyCode,
                        m = o.shiftKey,
                        i = o.which,
                        c = $(this);
                if (i === n.ENTER || i == n.NUMPAD_ENTER) {
                    h.saveCell(k);
                    o.preventDefault()
                } else {
                    if (i === n.TAB) {
                        if (f) {
                            var p = m ? c.index() - 1 : c.index() + 1;
                            if (p < 0 || (p === e.length)) {
                                h.tabCell(k, !m)
                            } else {
                                e.eq(p).focus()
                            }
                        } else {
                            h.tabCell(k, !m)
                        }
                        o.preventDefault()
                    } else {
                        if (i === n.ESCAPE) {
                            h.doCellEditCancelRequest(k);
                            o.preventDefault()
                        }
                    }
                }
            }).on("focus.datatable-cell click.datatable-cell", function (c) {
                h.currentCell = k
            })
        }
    },
    tabCell: function (a, d) {
        var b = d ? a.next() : a.prev();
        if (b.length == 0) {
            var c = d ? a.parent().next() : a.parent().prev();
            b = d ? c.children("td.ui-editable-column:first") : c.children("td.ui-editable-column:last")
        }
        this.showCellEditor(b)
    },
    saveCell: function (a) {
        var c = a.find("div.ui-cell-editor-input :input:enabled"),
                f = false,
                e = this;
        if (a.data("multi-edit")) {
            var b = a.data("old-value");
            for (var d = 0; d < c.length; d++) {
                if (c.eq(d).val() != b[d]) {
                    f = true;
                    break
                }
            }
        } else {
            f = (c.eq(0).val() != a.data("old-value"))
        }
        if (f) {
            e.doCellEditRequest(a)
        } else {
            e.viewMode(a)
        }
        this.currentCell = null
    },
    viewMode: function (a) {
        var b = a.children("div.ui-cell-editor"),
                d = b.children("div.ui-cell-editor-input"),
                c = b.children("div.ui-cell-editor-output");
        a.removeClass("ui-cell-editing ui-state-error ui-state-highlight");
        c.show();
        d.hide();
        a.removeData("old-value").removeData("multi-edit")
    },
    doCellEditRequest: function (a) {
        var h = this.getRowMeta(a.closest("tr")),
                e = a.children(".ui-cell-editor"),
                f = e.attr("id"),
                d = a.index(),
                c = h.index + "," + d,
                g = this;
        if (h.key) {
            c = c + "," + h.key
        }
        var b = {
            source: this.id,
            process: this.id,
            update: this.id,
            params: [{
                    name: this.id + "_encodeFeature",
                    value: true
                }, {
                    name: this.id + "_cellInfo",
                    value: c
                }, {
                    name: f,
                    value: f
                }],
            onsuccess: function (k, i, j) {
                PrimeFaces.ajax.Response.handle(k, i, j, {
                    widget: g,
                    handle: function (l) {
                        e.children(".ui-cell-editor-output").html(l)
                    }
                });
                return true
            },
            oncomplete: function (k, i, j) {
                if (j.validationFailed) {
                    a.addClass("ui-state-error")
                } else {
                    g.viewMode(a)
                }
            }
        };
        if (this.hasBehavior("cellEdit")) {
            this.cfg.behaviors.cellEdit.call(this, b)
        } else {
            PrimeFaces.ajax.Request.handle(b)
        }
    },
    doCellEditCancelRequest: function (a) {
        var g = this.getRowMeta(a.closest("tr")),
                e = a.children(".ui-cell-editor"),
                d = a.index(),
                c = g.index + "," + d,
                f = this;
        if (g.key) {
            c = c + "," + g.key
        }
        var b = {
            source: this.id,
            process: this.id,
            update: this.id,
            params: [{
                    name: this.id + "_encodeFeature",
                    value: true
                }, {
                    name: this.id + "_cellEditCancel",
                    value: true
                }, {
                    name: this.id + "_cellInfo",
                    value: c
                }],
            onsuccess: function (j, h, i) {
                PrimeFaces.ajax.Response.handle(j, h, i, {
                    widget: f,
                    handle: function (k) {
                        e.children(".ui-cell-editor-input").html(k)
                    }
                });
                return true
            },
            oncomplete: function (j, h, i) {
                f.viewMode(a);
                a.data("edit-events-bound", false);
                f.currentCell = null
            }
        };
        if (this.hasBehavior("cellEditCancel")) {
            this.cfg.behaviors.cellEditCancel.call(this, b)
        } else {
            PrimeFaces.ajax.Request.handle(b)
        }
    },
    saveRowEdit: function (a) {
        this.doRowEditRequest(a, "save")
    },
    cancelRowEdit: function (a) {
        this.doRowEditRequest(a, "cancel")
    },
    doRowEditRequest: function (a, d) {
        var f = a.closest("tr"),
                g = this.getRowMeta(f).index,
                b = f.hasClass("ui-expanded-row"),
                e = this,
                c = {
                    source: this.id,
                    process: this.id,
                    update: this.id,
                    formId: this.cfg.formId,
                    params: [{
                            name: this.id + "_rowEditIndex",
                            value: this.getRowMeta(f).index
                        }, {
                            name: this.id + "_rowEditAction",
                            value: d
                        }, {
                            name: this.id + "_encodeFeature",
                            value: true
                        }],
                    onsuccess: function (j, h, i) {
                        PrimeFaces.ajax.Response.handle(j, h, i, {
                            widget: e,
                            handle: function (k) {
                                if (b) {
                                    this.collapseRow(f)
                                }
                                this.updateRow(f, k)
                            }
                        });
                        return true
                    },
                    oncomplete: function (j, h, i) {
                        if (i && i.validationFailed) {
                            e.invalidateRow(g)
                        }
                    }
                };
        if (d === "save") {
            this.getRowEditors(f).each(function () {
                c.params.push({
                    name: this.id,
                    value: this.id
                })
            })
        }
        if (d === "save" && this.hasBehavior("rowEdit")) {
            this.cfg.behaviors.rowEdit.call(this, c)
        } else {
            if (d === "cancel" && this.hasBehavior("rowEditCancel")) {
                this.cfg.behaviors.rowEditCancel.call(this, c)
            } else {
                PrimeFaces.ajax.Request.handle(c)
            }
        }
    },
    updateRow: function (b, a) {
        b.replaceWith(a)
    },
    invalidateRow: function (a) {
        var b = (this.paginator) ? (a % this.paginator.getRows()) : a;
        this.tbody.children("tr").eq(b).addClass("ui-widget-content ui-row-editing ui-state-error")
    },
    getRowEditors: function (a) {
        return a.find("div.ui-cell-editor")
    },
    getPaginator: function () {
        return this.paginator
    },
    writeSelections: function () {
        $(this.selectionHolder).val(this.selection.join(","))
    },
    isSingleSelection: function () {
        return this.cfg.selectionMode == "single"
    },
    isMultipleSelection: function () {
        return this.cfg.selectionMode == "multiple" || this.isCheckboxSelectionEnabled()
    },
    clearSelection: function () {
        this.selection = [];
        $(this.selectionHolder).val("")
    },
    isSelectionEnabled: function () {
        return this.cfg.selectionMode != undefined || this.cfg.columnSelectionMode != undefined
    },
    isCheckboxSelectionEnabled: function () {
        return this.cfg.selectionMode === "checkbox"
    },
    isRadioSelectionEnabled: function () {
        return this.cfg.selectionMode === "radio"
    },
    clearFilters: function () {
        this.thead.find("> tr > th.ui-filter-column > .ui-column-filter").val("");
        $(this.jqId + "\\:globalFilter").val("");
        this.filter()
    },
    setupResizableColumns: function () {
        this.cfg.resizeMode = this.cfg.resizeMode || "fit";
        this.hasColumnGroup = this.hasColGroup();
        if (this.hasColumnGroup) {
            this.addGhostRow()
        }
        this.fixColumnWidths();
        if (!this.cfg.liveResize) {
            this.resizerHelper = $('<div class="ui-column-resizer-helper ui-state-highlight"></div>').appendTo(this.jq)
        }
        this.addResizers();
        var a = this.thead.find("> tr > th > span.ui-column-resizer"),
                b = this;
        a.draggable({
            axis: "x",
            start: function (d, e) {
                e.helper.data("originalposition", e.helper.offset());
                if (b.cfg.liveResize) {
                    b.jq.css("cursor", "col-resize")
                } else {
                    var f = b.cfg.stickyHeader ? b.clone : b.thead,
                            c = b.cfg.scrollable ? b.scrollBody.height() : f.parent().height() - f.height() - 1;
                    if (b.cfg.stickyHeader) {
                        c = c - b.relativeHeight
                    }
                    b.resizerHelper.height(c);
                    b.resizerHelper.show()
                }
            },
            drag: function (c, d) {
                if (b.cfg.liveResize) {
                    b.resize(c, d)
                } else {
                    b.resizerHelper.offset({
                        left: d.helper.offset().left + d.helper.width() / 2,
                        top: b.thead.offset().top + b.thead.height()
                    })
                }
            },
            stop: function (c, d) {
                d.helper.css({
                    left: "",
                    top: "0px"
                });
                if (b.cfg.liveResize) {
                    b.jq.css("cursor", "default")
                } else {
                    b.resize(c, d);
                    b.resizerHelper.hide()
                }
                if (b.cfg.resizeMode === "expand") {
                    setTimeout(function () {
                        b.fireColumnResizeEvent(d.helper.parent())
                    }, 5)
                } else {
                    b.fireColumnResizeEvent(d.helper.parent())
                }
                if (b.cfg.stickyHeader) {
                    b.reclone()
                }
            },
            containment: this.jq
        })
    },
    fireColumnResizeEvent: function (b) {
        if (this.hasBehavior("colResize")) {
            var a = {
                source: this.id,
                process: this.id,
                params: [{
                        name: this.id + "_colResize",
                        value: true
                    }, {
                        name: this.id + "_columnId",
                        value: b.attr("id")
                    }, {
                        name: this.id + "_width",
                        value: b.width()
                    }, {
                        name: this.id + "_height",
                        value: b.height()
                    }]
            };
            this.cfg.behaviors.colResize.call(this, a)
        }
    },
    hasColGroup: function () {
        return this.thead.children("tr").length > 1
    },
    addGhostRow: function () {
        var a = this.tbody.find("tr:first").children("td").length,
                c = "";
        for (var b = 0; b < a; b++) {
            c += '<th style="height:0px;border-bottom-width: 0px;border-top-width: 0px;padding-top: 0px;padding-bottom: 0px;outline: 0 none;" class="ui-resizable-column"></th>'
        }
        this.thead.prepend("<tr>" + c + "</tr>");
        if (this.cfg.scrollable) {
            this.theadClone.prepend("<tr>" + c + "</tr>");
            this.footerTable.children("tfoot").prepend("<tr>" + c + "</tr>")
        }
    },
    findGroupResizer: function (b) {
        for (var a = 0; a < this.groupResizers.length; a++) {
            var c = this.groupResizers.eq(a);
            if (c.offset().left === b.helper.data("originalposition").left) {
                return c
            }
        }
        return null
    },
    addResizers: function () {
        var a = this.thead.find("> tr > th.ui-resizable-column");
        a.prepend('<span class="ui-column-resizer">&nbsp;</span>');
        if (this.cfg.resizeMode === "fit") {
            a.filter(":last-child").children("span.ui-column-resizer").hide()
        }
        if (this.hasColumnGroup) {
            this.groupResizers = this.thead.find("> tr:first > th > .ui-column-resizer")
        }
    },
    resize: function (b, l) {
        var d, f, k = null,
                e = null,
                g = null,
                n = (this.cfg.resizeMode === "expand"),
                o = this.thead.parent();
        if (this.hasColumnGroup) {
            var p = this.findGroupResizer(l);
            if (!p) {
                return
            }
            d = p.parent()
        } else {
            d = l.helper.parent()
        }
        var f = d.nextAll(":visible:first");
        if (this.cfg.liveResize) {
            k = d.outerWidth() - (b.pageX - d.offset().left), e = (d.width() - k), g = (f.width() + k)
        } else {
            k = (l.position.left - l.originalPosition.left), e = (d.width() + k), g = (f.width() - k)
        }
        var a = parseInt(d.css("min-width"));
        a = (a == 0) ? 15 : a;
        if ((e > a && g > a) || (n && e > a)) {
            if (n) {
                o.width(o.width() + k);
                setTimeout(function () {
                    d.width(e)
                }, 1)
            } else {
                d.width(e);
                f.width(g)
            }
            if (this.cfg.scrollable) {
                var j = this.theadClone.parent(),
                        m = d.index();
                if (n) {
                    var i = this;
                    j.width(j.width() + k);
                    this.footerTable.width(this.footerTable.width() + k);
                    setTimeout(function () {
                        if (i.hasColumnGroup) {
                            i.theadClone.find("> tr:first").children("th").eq(m).width(e);
                            i.footerTable.find("> tfoot > tr:first").children("th").eq(m).width(e)
                        } else {
                            i.theadClone.find(PrimeFaces.escapeClientId(d.attr("id") + "_clone")).width(e);
                            i.footerCols.eq(m).width(e)
                        }
                    }, 1)
                } else {
                    if (this.hasColumnGroup) {
                        this.theadClone.find("> tr:first").children("th").eq(m).width(e);
                        this.theadClone.find("> tr:first").children("th").eq(m + 1).width(g);
                        this.footerTable.find("> tfoot > tr:first").children("th").eq(m).width(e);
                        this.footerTable.find("> tfoot > tr:first").children("th").eq(m + 1).width(g)
                    } else {
                        this.theadClone.find(PrimeFaces.escapeClientId(d.attr("id") + "_clone")).width(e);
                        this.theadClone.find(PrimeFaces.escapeClientId(f.attr("id") + "_clone")).width(g);
                        if (this.footerCols.length > 0) {
                            var h = this.footerCols.eq(m),
                                    c = h.next();
                            h.width(e);
                            c.width(g)
                        }
                    }
                }
            }
        }
    },
    hasBehavior: function (a) {
        if (this.cfg.behaviors) {
            return this.cfg.behaviors[a] != undefined
        }
        return false
    },
    removeSelection: function (a) {
        this.selection = $.grep(this.selection, function (b) {
            return b != a
        })
    },
    addSelection: function (a) {
        if (!this.isSelected(a)) {
            this.selection.push(a)
        }
    },
    isSelected: function (a) {
        return PrimeFaces.inArray(this.selection, a)
    },
    getRowMeta: function (b) {
        var a = {
            index: b.data("ri"),
            key: b.attr("data-rk")
        };
        return a
    },
    setupDraggableColumns: function () {
        this.orderStateHolder = $(this.jqId + "_columnOrder");
        this.saveColumnOrder();
        this.dragIndicatorTop = $('<span class="ui-icon ui-icon-arrowthick-1-s" style="position:absolute"/></span>').hide().appendTo(this.jq);
        this.dragIndicatorBottom = $('<span class="ui-icon ui-icon-arrowthick-1-n" style="position:absolute"/></span>').hide().appendTo(this.jq);
        var a = this;
        $(this.jqId + " thead th").draggable({
            appendTo: "body",
            opacity: 0.75,
            cursor: "move",
            scope: this.id,
            cancel: ":input,.ui-column-resizer",
            start: function (b, c) {
                c.helper.css("z-index", ++PrimeFaces.zindex)
            },
            drag: function (e, g) {
                var i = g.helper.data("droppable-column");
                if (i) {
                    var d = i.offset(),
                            b = d.top - 10,
                            c = d.top + i.height() + 8,
                            f = null;
                    if (e.originalEvent.pageX >= d.left + (i.width() / 2)) {
                        var h = i.next();
                        if (h.length == 1) {
                            f = h.offset().left - 9
                        } else {
                            f = i.offset().left + i.innerWidth() - 9
                        }
                        g.helper.data("drop-location", 1)
                    } else {
                        f = d.left - 9;
                        g.helper.data("drop-location", -1)
                    }
                    a.dragIndicatorTop.offset({
                        left: f,
                        top: b - 3
                    }).show();
                    a.dragIndicatorBottom.offset({
                        left: f,
                        top: c - 3
                    }).show()
                }
            },
            stop: function (b, c) {
                a.dragIndicatorTop.css({
                    left: 0,
                    top: 0
                }).hide();
                a.dragIndicatorBottom.css({
                    left: 0,
                    top: 0
                }).hide()
            },
            helper: function () {
                var c = $(this),
                        b = $('<div class="ui-widget ui-state-default" style="padding:4px 10px;text-align:center;"></div>');
                b.width(c.width());
                b.height(c.height());
                b.html(c.html());
                return b.get(0)
            }
        }).droppable({
            hoverClass: "ui-state-highlight",
            tolerance: "pointer",
            scope: this.id,
            over: function (b, c) {
                c.helper.data("droppable-column", $(this))
            },
            drop: function (c, j) {
                var n = j.draggable,
                        f = j.helper.data("drop-location"),
                        g = $(this),
                        e = null,
                        l = null;
                var k = a.tbody.find("> tr:not(.ui-expanded-row-content) > td:nth-child(" + (n.index() + 1) + ")"),
                        m = a.tbody.find("> tr:not(.ui-expanded-row-content) > td:nth-child(" + (g.index() + 1) + ")");
                if (a.tfoot.length) {
                    var b = a.tfoot.find("> tr > td"),
                            e = b.eq(n.index()),
                            l = b.eq(g.index())
                }
                if (f > 0) {
                    if (a.cfg.resizableColumns) {
                        if (g.next().length) {
                            g.children("span.ui-column-resizer").show();
                            n.children("span.ui-column-resizer").hide()
                        }
                    }
                    n.insertAfter(g);
                    k.each(function (o, p) {
                        $(this).insertAfter(m.eq(o))
                    });
                    if (e && l) {
                        e.insertAfter(l)
                    }
                    if (a.cfg.scrollable) {
                        var h = $(document.getElementById(n.attr("id") + "_clone")),
                                d = $(document.getElementById(g.attr("id") + "_clone"));
                        h.insertAfter(d)
                    }
                } else {
                    n.insertBefore(g);
                    k.each(function (o, p) {
                        $(this).insertBefore(m.eq(o))
                    });
                    if (e && l) {
                        e.insertBefore(l)
                    }
                    if (a.cfg.scrollable) {
                        var h = $(document.getElementById(n.attr("id") + "_clone")),
                                d = $(document.getElementById(g.attr("id") + "_clone"));
                        h.insertBefore(d)
                    }
                }
                a.saveColumnOrder();
                if (a.cfg.behaviors) {
                    var i = a.cfg.behaviors.colReorder;
                    if (i) {
                        i.call(a)
                    }
                }
            }
        })
    },
    saveColumnOrder: function () {
        var a = [],
                b = $(this.jqId + " thead:first th");
        b.each(function (c, d) {
            a.push($(d).attr("id"))
        });
        this.orderStateHolder.val(a.join(","))
    },
    makeRowsDraggable: function () {
        var a = this;
        this.tbody.sortable({
            placeholder: "ui-datatable-rowordering ui-state-active",
            cursor: "move",
            handle: "td,span:not(.ui-c)",
            appendTo: document.body,
            start: function (b, c) {
                c.helper.css("z-index", ++PrimeFaces.zindex)
            },
            helper: function (g, h) {
                var d = h.children(),
                        f = $('<div class="ui-datatable ui-widget"><table><tbody></tbody></table></div>'),
                        c = h.clone(),
                        b = c.children();
                for (var e = 0; e < b.length; e++) {
                    b.eq(e).width(d.eq(e).width())
                }
                c.appendTo(f.find("tbody"));
                return f
            },
            update: function (d, e) {
                var c = e.item.data("ri"),
                        f = a.paginator ? a.paginator.getFirst() + e.item.index() : e.item.index();
                a.syncRowParity();
                var b = {
                    source: a.id,
                    process: a.id,
                    params: [{
                            name: a.id + "_rowreorder",
                            value: true
                        }, {
                            name: a.id + "_fromIndex",
                            value: c
                        }, {
                            name: a.id + "_toIndex",
                            value: f
                        }, {
                            name: this.id + "_skipChildren",
                            value: true
                        }]
                };
                if (a.hasBehavior("rowReorder")) {
                    a.cfg.behaviors.rowReorder.call(a, b)
                } else {
                    PrimeFaces.ajax.Request.handle(b)
                }
            },
            change: function (b, c) {
                if (a.cfg.scrollable) {
                    PrimeFaces.scrollInView(a.scrollBody, c.placeholder)
                }
            }
        })
    },
    syncRowParity: function () {
        var b = this.tbody.children("tr.ui-widget-content"),
                d = this.paginator ? this.paginator.getFirst() : 0;
        for (var a = d; a < b.length; a++) {
            var c = b.eq(a);
            c.data("ri", a).removeClass("ui-datatable-even ui-datatable-odd");
            if (a % 2 === 0) {
                c.addClass("ui-datatable-even")
            } else {
                c.addClass("ui-datatable-odd")
            }
        }
    },
    isEmpty: function () {
        return this.tbody.children("tr.ui-datatable-empty-message").length === 1
    },
    getSelectedRowsCount: function () {
        return this.isSelectionEnabled() ? this.selection.length : 0
    },
    updateHeaderCheckbox: function () {
        if (this.isEmpty()) {
            this.uncheckHeaderCheckbox();
            this.disableHeaderCheckbox()
        } else {
            var b, d, c, a;
            if (this.cfg.nativeElements) {
                b = this.tbody.find("> tr > td.ui-selection-column > :checkbox");
                c = b.filter(":enabled");
                a = b.filter(":disabled");
                d = c.filter(":checked")
            } else {
                b = this.tbody.find("> tr > td.ui-selection-column .ui-chkbox-box");
                c = b.filter(":not(.ui-state-disabled)");
                a = b.filter(".ui-state-disabled");
                d = c.prev().children(":checked")
            }
            if (c.length && c.length === d.length) {
                this.checkHeaderCheckbox()
            } else {
                this.uncheckHeaderCheckbox()
            }
            if (b.length === a.length) {
                this.disableHeaderCheckbox()
            } else {
                this.enableHeaderCheckbox()
            }
        }
    },
    checkHeaderCheckbox: function () {
        if (this.cfg.nativeElements) {
            this.checkAllToggler.prop("checked", true)
        } else {
            this.checkAllToggler.addClass("ui-state-active").children("span.ui-chkbox-icon").removeClass("ui-icon-blank").addClass("ui-icon-check");
            this.checkAllTogglerInput.prop("checked", true).attr("aria-checked", true)
        }
    },
    uncheckHeaderCheckbox: function () {
        if (this.cfg.nativeElements) {
            this.checkAllToggler.prop("checked", false)
        } else {
            this.checkAllToggler.removeClass("ui-state-active").children("span.ui-chkbox-icon").addClass("ui-icon-blank").removeClass("ui-icon-check");
            this.checkAllTogglerInput.prop("checked", false).attr("aria-checked", false)
        }
    },
    disableHeaderCheckbox: function () {
        if (this.cfg.nativeElements) {
            this.checkAllToggler.prop("disabled", true)
        } else {
            this.checkAllToggler.addClass("ui-state-disabled")
        }
    },
    enableHeaderCheckbox: function () {
        if (this.cfg.nativeElements) {
            this.checkAllToggler.prop("disabled", false)
        } else {
            this.checkAllToggler.removeClass("ui-state-disabled")
        }
    },
    setupStickyHeader: function () {
        var b = this.thead.parent(),
                f = b.offset(),
                d = $(window),
                c = this,
                e = "scroll." + this.id,
                a = "resize.sticky-" + this.id;
        this.stickyContainer = $('<div class="ui-datatable ui-datatable-sticky ui-widget"><table></table></div>');
        this.clone = this.thead.clone(false);
        this.stickyContainer.children("table").append(this.thead);
        b.prepend(this.clone);
        this.stickyContainer.css({
            position: "absolute",
            width: b.outerWidth(),
            top: f.top,
            left: f.left,
            "z-index": ++PrimeFaces.zindex
        });
        this.jq.prepend(this.stickyContainer);
        if (this.cfg.resizableColumns) {
            this.relativeHeight = 0
        }
        d.off(e).on(e, function () {
            var h = d.scrollTop(),
                    g = b.offset();
            if (h > g.top) {
                c.stickyContainer.css({
                    position: "fixed",
                    top: "0px"
                }).addClass("ui-shadow ui-sticky");
                if (c.cfg.resizableColumns) {
                    c.relativeHeight = h - g.top
                }
                if (h >= (g.top + c.tbody.height())) {
                    c.stickyContainer.hide()
                } else {
                    c.stickyContainer.show()
                }
            } else {
                c.stickyContainer.css({
                    position: "absolute",
                    top: g.top
                }).removeClass("ui-shadow ui-sticky");
                if (c.stickyContainer.is(":hidden")) {
                    c.stickyContainer.show()
                }
                if (c.cfg.resizableColumns) {
                    c.relativeHeight = 0
                }
            }
        }).off(a).on(a, function () {
            c.stickyContainer.width(b.outerWidth())
        });
        this.clone.find(".ui-column-filter").prop("disabled", true)
    },
    getFocusableTbody: function () {
        return this.tbody
    },
    reclone: function () {
        this.clone.remove();
        this.clone = this.thead.clone(false);
        this.jq.find(".ui-datatable-tablewrapper > table").prepend(this.clone)
    },
    addRow: function () {
        var b = this,
                a = {
                    source: this.id,
                    process: this.id,
                    update: this.id,
                    params: [{
                            name: this.id + "_addrow",
                            value: true
                        }, {
                            name: this.id + "_skipChildren",
                            value: true
                        }, {
                            name: this.id + "_encodeFeature",
                            value: true
                        }],
                    onsuccess: function (e, c, d) {
                        PrimeFaces.ajax.Response.handle(e, c, d, {
                            widget: b,
                            handle: function (f) {
                                this.tbody.append(f)
                            }
                        });
                        return true
                    }
                };
        PrimeFaces.ajax.Request.handle(a)
    }
});
PrimeFaces.widget.FrozenDataTable = PrimeFaces.widget.DataTable.extend({
    setupScrolling: function () {
        this.scrollLayout = this.jq.find("> table > tbody > tr > td.ui-datatable-frozenlayout-right");
        this.frozenLayout = this.jq.find("> table > tbody > tr > td.ui-datatable-frozenlayout-left");
        this.scrollContainer = this.jq.find("> table > tbody > tr > td.ui-datatable-frozenlayout-right > .ui-datatable-scrollable-container");
        this.frozenContainer = this.jq.find("> table > tbody > tr > td.ui-datatable-frozenlayout-left > .ui-datatable-frozen-container");
        this.scrollHeader = this.scrollContainer.children(".ui-datatable-scrollable-header");
        this.scrollHeaderBox = this.scrollHeader.children("div.ui-datatable-scrollable-header-box");
        this.scrollBody = this.scrollContainer.children(".ui-datatable-scrollable-body");
        this.scrollFooter = this.scrollContainer.children(".ui-datatable-scrollable-footer");
        this.scrollFooterBox = this.scrollFooter.children("div.ui-datatable-scrollable-footer-box");
        this.scrollStateHolder = $(this.jqId + "_scrollState");
        this.scrollHeaderTable = this.scrollHeaderBox.children("table");
        this.scrollBodyTable = this.scrollBody.children("table");
        this.scrollThead = this.thead.eq(1);
        this.scrollTbody = this.tbody.eq(1);
        this.scrollFooterTable = this.scrollFooterBox.children("table");
        this.scrollFooterCols = this.scrollFooter.find("> .ui-datatable-scrollable-footer-box > table > tfoot > tr > td");
        this.frozenHeader = this.frozenContainer.children(".ui-datatable-scrollable-header");
        this.frozenBody = this.frozenContainer.children(".ui-datatable-scrollable-body");
        this.frozenBodyTable = this.frozenBody.children("table");
        this.frozenThead = this.thead.eq(0);
        this.frozenTbody = this.tbody.eq(0);
        this.frozenFooter = this.frozenContainer.children(".ui-datatable-scrollable-footer");
        this.frozenFooterTable = this.frozenFooter.find("> .ui-datatable-scrollable-footer-box > table");
        this.frozenFooterCols = this.frozenFooter.find("> .ui-datatable-scrollable-footer-box > table > tfoot > tr > td");
        this.percentageScrollHeight = this.cfg.scrollHeight && (this.cfg.scrollHeight.indexOf("%") !== -1);
        this.percentageScrollWidth = this.cfg.scrollWidth && (this.cfg.scrollWidth.indexOf("%") !== -1);
        this.frozenThead.find("> tr > th").addClass("ui-frozen-column");
        var c = this,
                b = this.getScrollbarWidth() + "px";
        if (this.cfg.scrollHeight) {
            if (this.percentageScrollHeight) {
                this.adjustScrollHeight()
            }
            if (this.hasVerticalOverflow()) {
                this.scrollHeaderBox.css("margin-right", b);
                this.scrollFooterBox.css("margin-right", b)
            }
        }
        if (this.cfg.selectionMode) {
            this.scrollTbody.removeAttr("tabindex")
        }
        this.fixColumnWidths();
        if (this.cfg.scrollWidth) {
            if (this.percentageScrollWidth) {
                this.adjustScrollWidth()
            } else {
                this.setScrollWidth(parseInt(this.cfg.scrollWidth))
            }
            if (this.hasVerticalOverflow()) {
                if (PrimeFaces.env.browser.webkit === true) {
                    this.frozenBody.append('<div style="height:' + b + ';border:1px solid transparent"></div>')
                } else {
                    if (PrimeFaces.isIE(8)) {
                        this.frozenBody.append('<div style="height:' + b + '"></div>')
                    } else {
                        this.frozenBodyTable.css("margin-bottom", b)
                    }
                }
            }
        }
        this.cloneHead();
        this.restoreScrollState();
        if (this.cfg.liveScroll) {
            this.scrollOffset = 0;
            this.cfg.liveScrollBuffer = (100 - this.cfg.liveScrollBuffer) / 100;
            this.shouldLiveScroll = true;
            this.loadingLiveScroll = false;
            this.allLoadedLiveScroll = c.cfg.scrollStep >= c.cfg.scrollLimit
        }
        this.scrollBody.scroll(function () {
            var g = c.scrollBody.scrollLeft(),
                    f = c.scrollBody.scrollTop();
            c.scrollHeaderBox.css("margin-left", -g);
            c.scrollFooterBox.css("margin-left", -g);
            c.frozenBody.scrollTop(f);
            if (c.shouldLiveScroll) {
                var f = Math.ceil(this.scrollTop),
                        e = this.scrollHeight,
                        d = this.clientHeight;
                if ((f >= ((e * c.cfg.liveScrollBuffer) - (d))) && c.shouldLoadLiveScroll()) {
                    c.loadLiveRows()
                }
            }
            c.saveScrollState()
        });
        var a = "resize." + this.id;
        $(window).unbind(a).bind(a, function () {
            if (c.jq.is(":visible")) {
                if (c.percentageScrollHeight) {
                    c.adjustScrollHeight()
                }
                if (c.percentageScrollWidth) {
                    c.adjustScrollWidth()
                }
            }
        })
    },
    cloneHead: function () {
        this.frozenTheadClone = this.frozenThead.clone();
        this.frozenTheadClone.find("th").each(function () {
            var a = $(this);
            a.attr("id", a.attr("id") + "_clone");
            $(this).children().not(".ui-column-title").remove()
        });
        this.frozenTheadClone.removeAttr("id").addClass("ui-datatable-scrollable-theadclone").height(0).prependTo(this.frozenBodyTable);
        this.scrollTheadClone = this.scrollThead.clone();
        this.scrollTheadClone.find("th").each(function () {
            var a = $(this);
            a.attr("id", a.attr("id") + "_clone");
            $(this).children().not(".ui-column-title").remove()
        });
        this.scrollTheadClone.removeAttr("id").addClass("ui-datatable-scrollable-theadclone").height(0).prependTo(this.scrollBodyTable)
    },
    hasVerticalOverflow: function () {
        return this.scrollBodyTable.outerHeight() > this.scrollBody.outerHeight()
    },
    adjustScrollHeight: function () {
        var d = this.jq.parent().innerHeight() * (parseInt(this.cfg.scrollHeight) / 100),
                f = this.jq.children(".ui-datatable-header").outerHeight(true),
                b = this.jq.children(".ui-datatable-footer").outerHeight(true),
                c = (this.scrollHeader.innerHeight() + this.scrollFooter.innerHeight()),
                e = this.paginator ? this.paginator.getContainerHeight(true) : 0,
                a = (d - (c + e + f + b));
        this.scrollBody.height(a);
        this.frozenBody.height(a)
    },
    adjustScrollWidth: function () {
        var a = parseInt((this.scrollLayout.innerWidth() * (parseInt(this.cfg.scrollWidth) / 100)));
        this.setScrollWidth(a)
    },
    setScrollWidth: function (b) {
        var c = this,
                a = b + this.frozenLayout.width();
        this.jq.children(".ui-widget-header").each(function () {
            c.setOuterWidth($(this), a)
        });
        this.scrollHeader.width(b);
        this.scrollBody.css("margin-right", 0).width(b);
        this.scrollFooter.width(b)
    },
    fixColumnWidths: function () {
        if (!this.columnWidthsFixed) {
            if (PrimeFaces.isIE(7)) {
                this.bodyTable.css("width", "auto")
            }
            if (this.cfg.scrollable) {
                this._fixColumnWidths(this.scrollHeader, this.scrollFooterCols, this.scrollColgroup);
                this._fixColumnWidths(this.frozenHeader, this.frozenFooterCols, this.frozenColgroup)
            } else {
                this.jq.find("> .ui-datatable-tablewrapper > table > thead > tr > th").each(function () {
                    var a = $(this);
                    a.width(a.width())
                })
            }
            this.columnWidthsFixed = true
        }
    },
    _fixColumnWidths: function (b, a) {
        b.find("> .ui-datatable-scrollable-header-box > table > thead > tr > th").each(function () {
            var f = $(this),
                    c = f.index(),
                    d = f.width();
            f.width(d);
            if (a.length > 0) {
                var e = a.eq(c);
                e.width(d)
            }
        })
    },
    updateData: function (c, e) {
        var k = $("<table><tbody>" + c + "</tbody></table>"),
                m = k.find("> tbody > tr"),
                g = (e === undefined) ? true : e;
        if (g) {
            this.frozenTbody.children().remove();
            this.scrollTbody.children().remove()
        }
        var b = this.frozenTbody.children("tr:first"),
                h = b.length ? b.children("td").length : this.cfg.frozenColumns;
        for (var d = 0; d < m.length; d++) {
            var l = m.eq(d),
                    a = l.children("td"),
                    j = this.copyRow(l),
                    f = this.copyRow(l);
            j.append(a.slice(0, h));
            f.append(a.slice(h));
            this.frozenTbody.append(j);
            this.scrollTbody.append(f)
        }
        this.postUpdateData()
    },
    copyRow: function (a) {
        return $("<tr></tr>").data("ri", a.data("ri")).attr("data-rk", a.data("rk")).addClass(a.attr("class")).attr("role", "row")
    },
    getThead: function () {
        return $(this.jqId + "_frozenThead," + this.jqId + "_scrollableThead")
    },
    getTbody: function () {
        return $(this.jqId + "_frozenTbody," + this.jqId + "_scrollableTbody")
    },
    getTfoot: function () {
        return $(this.jqId + "_frozenTfoot," + this.jqId + "_scrollableTfoot")
    },
    bindRowHover: function (a) {
        var b = this;
        this.tbody.off("mouseover.datatable mouseout.datatable", a).on("mouseover.datatable", a, null, function () {
            var c = $(this),
                    d = b.getTwinRow(c);
            if (!c.hasClass("ui-state-highlight")) {
                c.addClass("ui-state-hover");
                d.addClass("ui-state-hover")
            }
        }).on("mouseout.datatable", a, null, function () {
            var c = $(this),
                    d = b.getTwinRow(c);
            if (!c.hasClass("ui-state-highlight")) {
                c.removeClass("ui-state-hover");
                d.removeClass("ui-state-hover")
            }
        })
    },
    getTwinRow: function (b) {
        var a = (this.tbody.index(b.parent()) === 0) ? this.tbody.eq(1) : this.tbody.eq(0);
        return a.children().eq(b.index())
    },
    highlightRow: function (a) {
        this._super(a);
        this._super(this.getTwinRow(a))
    },
    unhighlightRow: function (a) {
        this._super(a);
        this._super(this.getTwinRow(a))
    },
    displayExpandedRow: function (b, a) {
        var d = this.getTwinRow(b);
        b.after(a);
        var c = b.next();
        c.show();
        d.after('<tr class="ui-expanded-row-content ui-widget-content"><td></td></tr>');
        d.next().children("td").attr("colspan", d.children("td").length).height(c.children("td").height())
    },
    collapseRow: function (a) {
        this._super(a);
        this._super(this.getTwinRow(a))
    },
    getExpandedRows: function () {
        return this.frozenTbody.children(".ui-expanded-row")
    },
    showRowEditors: function (a) {
        this._super(a);
        this._super(this.getTwinRow(a))
    },
    updateRow: function (g, e) {
        var d = $("<table><tbody>" + e + "</tbody></table>"),
                b = d.find("> tbody > tr"),
                c = b.children("td"),
                a = this.copyRow(b),
                f = this.copyRow(b),
                h = this.getTwinRow(g);
        a.append(c.slice(0, this.cfg.frozenColumns));
        f.append(c.slice(this.cfg.frozenColumns));
        g.replaceWith(a);
        h.replaceWith(f)
    },
    invalidateRow: function (a) {
        this.frozenTbody.children("tr").eq(a).addClass("ui-widget-content ui-row-editing ui-state-error");
        this.scrollTbody.children("tr").eq(a).addClass("ui-widget-content ui-row-editing ui-state-error")
    },
    getRowEditors: function (a) {
        return a.find("div.ui-cell-editor").add(this.getTwinRow(a).find("div.ui-cell-editor"))
    },
    findGroupResizer: function (a) {
        var b = this._findGroupResizer(a, this.frozenGroupResizers);
        if (b) {
            return b
        } else {
            return this._findGroupResizer(a, this.scrollGroupResizers)
        }
    },
    _findGroupResizer: function (c, a) {
        for (var b = 0; b < a.length; b++) {
            var d = a.eq(b);
            if (d.offset().left === c.helper.data("originalposition").left) {
                return d
            }
        }
        return null
    },
    addResizers: function () {
        var b = this.frozenThead.find("> tr > th.ui-resizable-column"),
                a = this.scrollThead.find("> tr > th.ui-resizable-column");
        b.prepend('<span class="ui-column-resizer">&nbsp;</span>');
        a.prepend('<span class="ui-column-resizer">&nbsp;</span>');
        if (this.cfg.resizeMode === "fit") {
            b.filter(":last-child").addClass("ui-frozen-column-last");
            a.filter(":last-child").children("span.ui-column-resizer").hide()
        }
        if (this.hasColumnGroup) {
            this.frozenGroupResizers = this.frozenThead.find("> tr:first > th > .ui-column-resizer");
            this.scrollGroupResizers = this.scrollThead.find("> tr:first > th > .ui-column-resizer")
        }
    },
    resize: function (q, n) {
        var s = null,
                i = null,
                j = null,
                o = null,
                c = (this.cfg.resizeMode === "expand");
        if (this.hasColumnGroup) {
            var p = this.findGroupResizer(n);
            if (!p) {
                return
            }
            s = p.parent()
        } else {
            s = n.helper.parent()
        }
        var g = s.next();
        var m = s.index(),
                b = s.hasClass("ui-frozen-column-last");
        if (this.cfg.liveResize) {
            i = s.outerWidth() - (q.pageX - s.offset().left), j = (s.width() - i), o = (g.width() + i)
        } else {
            i = (n.position.left - n.originalPosition.left), j = (s.width() + i), o = (g.width() - i)
        }
        var l = parseInt(s.css("min-width"));
        l = (l == 0) ? 15 : l;
        var e = (c && j > l) || (b ? (j > l) : (j > l && o > l));
        if (e) {
            var h = s.hasClass("ui-frozen-column"),
                    k = h ? this.frozenTheadClone : this.scrollTheadClone,
                    a = h ? this.frozenThead.parent() : this.scrollThead.parent(),
                    d = k.parent(),
                    v = h ? this.frozenFooterCols : this.scrollFooterCols,
                    u = h ? this.frozenFooterTable : this.scrollFooterTable,
                    f = this;
            if (c) {
                if (b) {
                    this.frozenLayout.width(this.frozenLayout.width() + i)
                }
                a.width(a.width() + i);
                d.width(d.width() + i);
                u.width(u.width() + i);
                setTimeout(function () {
                    s.width(j);
                    if (f.hasColumnGroup) {
                        k.find("> tr:first").children("th").eq(m).width(j);
                        u.find("> tfoot > tr:first").children("th").eq(m).width(j)
                    } else {
                        k.find(PrimeFaces.escapeClientId(s.attr("id") + "_clone")).width(j);
                        v.eq(m).width(j)
                    }
                }, 1)
            } else {
                if (b) {
                    this.frozenLayout.width(this.frozenLayout.width() + i)
                }
                s.width(j);
                g.width(o);
                if (this.hasColumnGroup) {
                    k.find("> tr:first").children("th").eq(m).width(j);
                    k.find("> tr:first").children("th").eq(m + 1).width(o);
                    u.find("> tfoot > tr:first").children("th").eq(m).width(j);
                    u.find("> tfoot > tr:first").children("th").eq(m + 1).width(o)
                } else {
                    k.find(PrimeFaces.escapeClientId(s.attr("id") + "_clone")).width(j);
                    k.find(PrimeFaces.escapeClientId(g.attr("id") + "_clone")).width(o);
                    if (v.length > 0) {
                        var t = v.eq(m),
                                r = t.next();
                        t.width(j);
                        r.width(o)
                    }
                }
            }
        }
    },
    hasColGroup: function () {
        return this.frozenThead.children("tr").length > 1 || this.scrollThead.children("tr").length > 1
    },
    addGhostRow: function () {
        this._addGhostRow(this.frozenTbody, this.frozenThead, this.frozenTheadClone, this.frozenFooter.find("table"), "ui-frozen-column");
        this._addGhostRow(this.scrollTbody, this.scrollThead, this.scrollTheadClone, this.scrollFooterTable)
    },
    _addGhostRow: function (g, e, f, h, c) {
        var b = g.find("tr:first").children("td"),
                a = b.length,
                j = "",
                k = c ? "ui-resizable-column " + c : "ui-resizable-column";
        for (var d = 0; d < a; d++) {
            j += '<th style="height:0px;border-bottom-width: 0px;border-top-width: 0px;padding-top: 0px;padding-bottom: 0px;outline: 0 none;width:' + b.eq(d).width() + 'px" class="' + k + '"></th>'
        }
        e.prepend("<tr>" + j + "</tr>");
        if (this.cfg.scrollable) {
            f.prepend("<tr>" + j + "</tr>");
            h.children("tfoot").prepend("<tr>" + j + "</tr>")
        }
    },
    getFocusableTbody: function () {
        return this.tbody.eq(0)
    },
    highlightFocusedRow: function () {
        this._super();
        this.getTwinRow(this.focusedRow).addClass("ui-state-hover")
    },
    unhighlightFocusedRow: function () {
        this._super();
        this.getTwinRow(this.focusedRow).removeClass("ui-state-hover")
    },
    assignFocusedRow: function (a) {
        this._super(a);
        if (!a.parent().attr("tabindex")) {
            this.frozenTbody.trigger("focus")
        }
    }
});