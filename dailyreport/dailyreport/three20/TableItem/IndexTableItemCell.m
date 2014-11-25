//
//  IndexTableItemCell.m
//  dailyreport
//
//  Created by zhang wei on 14-5-19.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "IndexTableItemCell.h"
#import "LinkImageView.h"
#import "StyleSheet.h"

@implementation IndexTableItemCell
@synthesize backgroundImageView = _backgroundImageView;
@synthesize indexItem = _indexItem;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString*)identifier {
    
    self = [super initWithStyle:style reuseIdentifier:identifier];
    if (self) {
        if([self respondsToSelector:@selector(setSeparatorInset:)]){
            [self setSeparatorInset:UIEdgeInsetsZero];
        }
        if([self respondsToSelector:@selector(setLayoutMargins:)]){
            [self setLayoutMargins:UIEdgeInsetsZero];
        }
        _backgroundImageView = [[LinkImageView alloc] initWithFrame:CGRectMake(0, 4, SCREEN_WIDTH, 0)];
        _backgroundImageView.backgroundColor = [UIColor blackColor];
        _label = [[TTStyledTextLabel alloc] init];

        _animation = TRUE;
        
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
    _label.userInteractionEnabled = NO;

    _label.font = [UIFont fontWithName:@"HelveticaNeue-UltraLight" size:40];
 
    _label.frame = CGRectMake(SCREEN_WIDTH*9/16, (self.backgroundImageView.frame.size.height-_label.font.lineHeight)/2, SCREEN_WIDTH*7/16-20,_label.font.lineHeight );


    if([self.indexItem.backgroundImage isEqualToString:@"首页童装.png"]){
        _label.textColor = [StyleSheet colorFromHexString:@"#505050"];
    }else{
        _label.textColor = [StyleSheet colorFromHexString:@"#ffffff"];
    }
    _label.contentMode = UIViewContentModeRight;
    _label.textAlignment = NSTextAlignmentRight;
    _label.backgroundColor = [UIColor clearColor];
    
    self.contentView.backgroundColor = [UIColor blackColor];
    
    if(_animation){
        _animation= NO;
        [NSTimer scheduledTimerWithTimeInterval:self.indexItem.index/4.0 target:self selector:@selector(doAnimation) userInfo:nil repeats:NO];
    }else{
        [self.contentView addSubview:self.backgroundImageView];
        [self.backgroundImageView addSubview:_label];
    }

    
}

-(void)doAnimation{
    [self.contentView addSubview:self.backgroundImageView];
    [self.backgroundImageView addSubview:_label];
    CABasicAnimation *animation = [CABasicAnimation  animationWithKeyPath:@"transform"];
    animation.fromValue = [NSValue valueWithCATransform3D:CATransform3DMakeRotation(-3.1415,1.0 ,0, 0)];
    animation.duration =0.25;
    animation.cumulative =YES;
    animation.repeatCount=1;
    
    [self.backgroundImageView.layer addAnimation:animation forKey:@"animation"];
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// TTTableViewCell


- (void)setObject:(id)object {
    self.indexItem = object;
    NSLog(@"%@",self.indexItem.backgroundImage);
    UIImage* image = [UIImage imageNamed:self.indexItem.backgroundImage];
    self.backgroundImageView.defaultImage = image;
    NSLog(@"%f",image.size.height);
    self.backgroundImageView.actionUrl = self.indexItem.URL;
    self.backgroundImageView.frame = CGRectMake(0, 4, SCREEN_WIDTH, image.size.height);
    
    [super setObject:self.indexItem.text];
}


///////////////////////////////////////////////////////////////////////////////////////////////////
- (id)object {
    return _indexItem;
}

@end
