//
//  VideoTableItemCell.m
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "VideoTableItemCell.h"

@implementation VideoTableItemCell

@synthesize videoImageView = _videoImageView;
@synthesize videoTableItem = _videoTableItem;

+ (CGFloat)tableView:(UITableView*)tableView rowHeightForObject:(id)object {
    return 80;
}


- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString*)identifier {
    
    self = [super initWithStyle:style reuseIdentifier:identifier];
    if (self) {
        self.videoImageView = [[TTImageView alloc] initWithFrame:CGRectZero];
    }
    
	return self;
}

- (void)dealloc {
    TT_RELEASE_SAFELY(_videoImageView);
	[super dealloc];
}
///////////////////////////////////////////////////////////////////////////////////////////////////
// UIView

- (void)layoutSubviews {
	[super layoutSubviews];
    self.videoImageView.frame = CGRectMake(10, 15, 50, 50);
    self.videoImageView.layer.cornerRadius = 8;
    self.videoImageView.layer.masksToBounds = YES;
}


///////////////////////////////////////////////////////////////////////////////////////////////////
// TTTableViewCell


- (void)setObject:(id)object {
    self.videoTableItem = object;
    self.videoImageView.urlPath = self.videoTableItem.video.picUrl;
    [super setObject:self.videoTableItem.text];
    
}


///////////////////////////////////////////////////////////////////////////////////////////////////
- (id)object {
    return self.videoTableItem;
}

@end
