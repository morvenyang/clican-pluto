//
//  CricleUIView.h
//  appletvserver
//
//  Created by zhang wei on 13-4-23.
//
//

#import <UIKit/UIKit.h>

@interface CricleUIView : UIView{
    UIColor* _color;
}
@property(nonatomic, retain)UIColor* color;
- (id)initWithFrame:(CGRect)frame color:(UIColor*)color;
@end
