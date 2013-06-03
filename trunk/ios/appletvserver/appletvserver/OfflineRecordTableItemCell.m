//
//  OfflineRecordTableItemCell.m
//  appletvserver
//
//  Created by zhang wei on 13-6-3.
//
//

#import "OfflineRecordTableItemCell.h"

@implementation OfflineRecordTableItemCell

@synthesize offlineRecordTableItem = _offlineRecordTableItem;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

- (void)dealloc {
    TT_RELEASE_SAFELY(_offlineRecordTableItem);
	[super dealloc];
}


- (void)setObject:(id)object {
    if(object){
        self.offlineRecordTableItem = object;
        [super setObject:object];
    }
}

- (id)object {
    return self.offlineRecordTableItem;
}

+ (CGFloat)tableView:(UITableView*)tableView rowHeightForObject:(id)object {
    CGFloat height =[super tableView:tableView rowHeightForObject:object];
    height = height + 25;
    return height;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    if(self.offlineRecordTableItem.deleteButton!=nil){
        self.offlineRecordTableItem.deleteButton.frame=CGRectMake(self.label.frame.origin.x, self.label.frame.origin.y+self.label.frame.size.height-25, 100, 25);
        [self.contentView addSubview:self.offlineRecordTableItem.deleteButton];
    }
    if(self.offlineRecordTableItem.actionButton!=nil){
        self.offlineRecordTableItem.actionButton.frame=CGRectMake(self.label.frame.origin.x+150, self.label.frame.origin.y+self.label.frame.size.height-25, 100, 25);
        [self.contentView addSubview:self.offlineRecordTableItem.actionButton];
    }
    
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
