//
//  IndexTableItemCell.m
//  dailyreport
//
//  Created by zhang wei on 14-5-19.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "IndexTableItemCell.h"
#import "LinkImageView.h"

@implementation IndexTableItemCell
@synthesize backgroundImageView = _backgroundImageView;
@synthesize indexItem = _indexItem;

+ (CGFloat)tableView:(UITableView*)tableView rowHeightForObject:(id)object {
    return 101;
}

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString*)identifier {
    
    self = [super initWithStyle:style reuseIdentifier:identifier];
    if (self) {
        
        _backgroundImageView = [[LinkImageView alloc] initWithFrame:CGRectMake(7, 0, 307, 101)];
        
        _label = [[TTStyledTextLabel alloc] init];
        self.contentView.backgroundColor = [UIColor blackColor];
        
        [self.contentView addSubview:self.backgroundImageView];
        [self.contentView addSubview:_label];
        
        
        self.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    
	return self;
}


- (void)dealloc {
    TT_RELEASE_SAFELY(_backgroundImageView);
    TT_RELEASE_SAFELY(_indexItem);
	[super dealloc];
}
///////////////////////////////////////////////////////////////////////////////////////////////////
// UIView

- (void)layoutSubviews {
	[super layoutSubviews];
    _label.frame = CGRectMake(20, 80, 270, 35);
    if([self.indexItem.backgroundImage isEqualToString:@"首页童装.png"]){
        _label.textColor = [UIColor blackColor];
    }else{
        _label.textColor = [UIColor whiteColor];
    }
    _label.font = [UIFont boldSystemFontOfSize:18];
    _label.contentMode = UIViewContentModeCenter;
    _label.backgroundColor = [UIColor clearColor];
}


///////////////////////////////////////////////////////////////////////////////////////////////////
// TTTableViewCell


- (void)setObject:(id)object {
    self.indexItem = object;
    NSLog(@"%@",self.indexItem.backgroundImage);
    self.backgroundImageView.urlPath=[NSString stringWithFormat:@"bundle://%@",self.indexItem.backgroundImage];
    self.backgroundImageView.actionUrl = self.indexItem.URL;
    self.backgroundImageView.frame = CGRectMake(7, 4, 307, 101);
    [super setObject:self.indexItem.text];
}


///////////////////////////////////////////////////////////////////////////////////////////////////
- (id)object {
    return _indexItem;
}

@end
