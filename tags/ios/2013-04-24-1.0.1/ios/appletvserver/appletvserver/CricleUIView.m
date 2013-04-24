//
//  CricleUIView.m
//  appletvserver
//
//  Created by zhang wei on 13-4-23.
//
//

#import "CricleUIView.h"

@implementation CricleUIView
@synthesize color = _color;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}
- (id)initWithFrame:(CGRect)frame color:(UIColor*)color{
    self = [super initWithFrame:frame];
    if (self) {
        self.color = color;
    }
    return self;
}

-(void)dealloc{
    TT_RELEASE_SAFELY(_color);
    [super dealloc];
}
- (void)drawRect:(CGRect)rect{
    /* Draw a circle */
    // Get the contextRef
    CGContextRef contextRef = UIGraphicsGetCurrentContext();
    
    // Set the border width
    CGContextSetLineWidth(contextRef, 1.0);
    
    // Set the circle fill color to GREEN
    [self.color setFill];
    
    // Fill the circle with the fill color
    CGContextFillEllipseInRect(contextRef, rect);

}

@end
